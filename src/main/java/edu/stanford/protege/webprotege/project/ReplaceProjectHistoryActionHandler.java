package edu.stanford.protege.webprotege.project;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.common.BlobLocation;
import edu.stanford.protege.webprotege.common.EventId;
import edu.stanford.protege.webprotege.common.ProjectEvent;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.event.LargeNumberOfChangesEvent;
import edu.stanford.protege.webprotege.ipc.EventDispatcher;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.storage.MinioFileDownloader;
import org.jetbrains.annotations.NotNull;
import org.semanticweb.binaryowl.BinaryOWLOntologyChangeLog;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Handles requests to replace a project's revision history with a BinaryOWL change log fetched
 * from object storage (MinIO/S3 compatible).
 *
 * <p><strong>Execution model</strong></p>
 * <ol>
 *   <li>Generate a correlation {@link EventId} if the request is accepted.</li>
 *   <li>Download the supplied blob to a local temp file.</li>
 *   <li>Validate the blob is a readable BinaryOWL change log.</li>
 *   <li>Replace the project's stored history.</li>
 *   <li>Delete the temp file (success or failure).</li>
 *   <li>On success, dispatch:
 *     <ul>
 *       <li>a {@link ReplaceProjectHistorySucceededEvent} (explicit domain event), and</li>
 *       <li>a packaged {@link LargeNumberOfChangesEvent} so clients refresh.</li>
 *     </ul>
 *     Both events reuse the same correlation {@link EventId} returned in the response.
 *   </li>
 *   <li>On failure, dispatch a {@link ReplaceProjectHistoryFailedEvent} with details (no packaged refresh event).</li>
 * </ol>
 *
 * <p><strong>Concurrency</strong></p>
 * <ul>
 *   <li>This handler instance is scoped to a single {@link ProjectId} and uses a lightweight, in-process
 *       guard to prevent overlapping replace operations for that project within the same JVM.</li>
 *   <li>When a request arrives while another replace is in progress, it is <em>not</em> started and the response
 *       carries {@code REJECTED_ALREADY_IN_PROGRESS}. The correlation id is {@code null} and no events follow.</li>
 *   <li>The guard only applies within the current JVM process; cross-node coordination (if running multiple
 *       app instances) is out of scope here.</li>
 * </ul>
 *
 * <p><strong>Response semantics</strong></p>
 * <ul>
 *   <li>On acceptance, an immediate response is returned with status {@code ACCEPTED} and a non-null correlation
 *       {@link EventId}. This id is reused by dispatched events so clients can correlate ACK → event(s).</li>
 *   <li>On rejection due to an in-flight operation, an immediate response is returned with status
 *       {@code REJECTED_ALREADY_IN_PROGRESS} and a {@code null} correlation id (no event will be dispatched).</li>
 * </ul>
 */
public class ReplaceProjectHistoryActionHandler extends AbstractProjectActionHandler<ReplaceProjectHistoryRequest, ReplaceProjectHistoryResponse> {

    private static final Logger logger = LoggerFactory.getLogger(ReplaceProjectHistoryActionHandler.class);

    private final ProjectHistoryReplacer projectHistoryReplacer;

    private final MinioFileDownloader minioFileDownloader;

    private final EventDispatcher eventDispatcher;

    private final OWLDataFactory dataFactory;

    private final Executor replaceProjectHistoryExecutor;

    /**
     * Per-{@link ProjectId} instance guard to prevent concurrent replaces in this JVM.
     */
    private final AtomicBoolean inProgress = new AtomicBoolean(false);

    /**
     * Creates a new handler.
     *
     * @param accessManager                 Access manager used by the base handler for auth checks.
     * @param projectHistoryReplacer        Component that swaps the project's stored history with the new one.
     * @param minioFileDownloader           Service to download blobs to local temporary files.
     * @param eventDispatcher               Event bus used to notify subscribers (domain + packaged events).
     * @param dataFactory                   OWL data factory used to validate the BinaryOWL stream.
     * @param replaceProjectHistoryExecutor Bounded executor on which the async pipeline runs.
     */
    public ReplaceProjectHistoryActionHandler(@NotNull AccessManager accessManager,
                                              ProjectHistoryReplacer projectHistoryReplacer,
                                              MinioFileDownloader minioFileDownloader,
                                              EventDispatcher eventDispatcher,
                                              OWLDataFactory dataFactory,
                                              @Qualifier("replaceProjectHistoryExecutor" ) Executor replaceProjectHistoryExecutor) {
        super(accessManager);
        this.projectHistoryReplacer = projectHistoryReplacer;
        this.minioFileDownloader = minioFileDownloader;
        this.eventDispatcher = eventDispatcher;
        this.dataFactory = dataFactory;
        this.replaceProjectHistoryExecutor = replaceProjectHistoryExecutor;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public Class<ReplaceProjectHistoryRequest> getActionClass() {
        return ReplaceProjectHistoryRequest.class;
    }

    /**
     * Starts an asynchronous replace of the project's revision history, returning immediately with an
     * {@link ReplaceProjectHistoryResponse} that indicates whether the request was accepted for processing.
     *
     * <p>If accepted, the response includes a correlation {@link EventId} that will be reused by the dispatched
     * domain and packaged events so clients can match the completion to this request.</p>
     *
     * <p>If a replace is already in progress for this handler's {@link ProjectId}, the request is rejected and
     * no work is started; the response contains {@code REJECTED_ALREADY_IN_PROGRESS} and a {@code null}
     * correlation id.</p>
     *
     * @param action           The request containing the {@link ProjectId} and the {@link BlobLocation}.
     * @param executionContext The execution context for the request.
     * @return A response with:
     * <ul>
     *   <li>{@code ACCEPTED} and a non-null correlation id when the replace has been scheduled asynchronously, or</li>
     *   <li>{@code REJECTED_ALREADY_IN_PROGRESS} and a {@code null} correlation id when another replace is running.</li>
     * </ul>
     * Subsequent success/failure (for accepted requests) is reported via events as described above.
     */
    @NotNull
    @Override
    public ReplaceProjectHistoryResponse execute(@NotNull ReplaceProjectHistoryRequest action,
                                                 @NotNull ExecutionContext executionContext) {
        var projectId = action.projectId();
        var blobLocation = action.projectHistoryLocation();

        // Fast, non-blocking concurrency guard per handler instance
        if (!inProgress.compareAndSet(false, true)) {
            logger.info("{} Replace already in progress; ignoring new request (bucket={} name={})" ,
                    projectId, blobLocation.bucket(), blobLocation.name());
            return new ReplaceProjectHistoryResponse(
                    projectId,
                    ReplaceProjectHistoryRequestStatus.REJECTED_ALREADY_IN_PROGRESS,
                    /* correlationId */ null
            );
        }

        var correlationId = EventId.generate();
        logger.info("{} Replacing project history (bucket={} name={}) [corrId={}]" ,
                projectId, blobLocation.bucket(), blobLocation.name(), correlationId);

        minioFileDownloader
                .downloadFile(blobLocation)
                .thenComposeAsync(path -> {
                    try {
                        checkRevisionHistoryLoads(path, projectId, blobLocation);
                        logger.info("{} Downloaded new project history to {} (bucket={} name={}) [corrId={}]" ,
                                projectId, path, blobLocation.bucket(), blobLocation.name(), correlationId);
                        return projectHistoryReplacer.replaceProjectHistory(path)
                                .whenComplete((__, ___) -> safeDelete(path, projectId));
                    } catch (Throwable t) {
                        // Validation failed: ensure we don't leak the file
                        safeDelete(path, projectId);
                        return CompletableFuture.failedFuture(t);
                    }
                }, replaceProjectHistoryExecutor)
                .thenRunAsync(() -> {
                    // Success: emit explicit domain event + packaged refresh event
                    fireReplaceSucceeded(projectId, correlationId, blobLocation);
                    fireProjectChangedEvent(projectId, correlationId);
                }, replaceProjectHistoryExecutor)
                .whenComplete((__, ___) -> inProgress.set(false)) // ALWAYS release the guard
                .exceptionally(ex -> {
                    // Failure: emit explicit failure domain event (no packaged refresh)
                    fireReplaceFailed(projectId, correlationId, blobLocation, ex);
                    logger.error("{} Replace project history failed (bucket={} name={}) [corrId={}]" ,
                            projectId, blobLocation.bucket(), blobLocation.name(), correlationId, ex);
                    return null;
                });

        // Immediate ACK – operation continues asynchronously
        return new ReplaceProjectHistoryResponse(
                projectId,
                ReplaceProjectHistoryRequestStatus.ACCEPTED,
                correlationId
        );
    }

    /**
     * Dispatches an explicit domain event indicating the replace completed successfully.
     *
     * @param projectId     The project whose history was replaced.
     * @param correlationId The correlation id shared with the response and packaged event.
     * @param blobLocation  The blob that was applied.
     */
    private void fireReplaceSucceeded(ProjectId projectId, EventId correlationId, BlobLocation blobLocation) {
        eventDispatcher.dispatchEvent(
                new ReplaceProjectHistorySucceededEvent(correlationId, projectId, blobLocation)
        );
    }

    /**
     * Dispatches an explicit domain event indicating the replace failed.
     *
     * @param projectId     The project whose replace failed.
     * @param correlationId The correlation id shared with the response.
     * @param blobLocation  The blob that was attempted.
     * @param cause         The failure cause (used for message extraction).
     */
    private void fireReplaceFailed(ProjectId projectId, EventId correlationId, BlobLocation blobLocation, Throwable cause) {
        eventDispatcher.dispatchEvent(
                new ReplaceProjectHistoryFailedEvent(
                        correlationId, projectId, blobLocation, cause == null ? null : cause.getMessage()
                )
        );
    }

    /**
     * Dispatches a packaged {@link LargeNumberOfChangesEvent} using the provided correlation id.
     * The same id is used for both the packaged event and the child event so clients can match
     * the event back to the originating request/response.
     *
     * @param projectId     The project whose clients should be notified.
     * @param correlationId The correlation id shared with the response and domain events.
     */
    private void fireProjectChangedEvent(ProjectId projectId, EventId correlationId) {
        logger.info("{} Firing project changed event [corrId={}]" , projectId, correlationId);
        var events = List.<ProjectEvent>of(new LargeNumberOfChangesEvent(correlationId, projectId));
        var packaged = new PackagedProjectChangeEvent(projectId, correlationId, events);
        eventDispatcher.dispatchEvent(packaged);
    }

    /**
     * Attempts to delete a temporary file, logging success or failure. This method never throws.
     *
     * @param path      The path to delete.
     * @param projectId The project id used for log context.
     */
    private void safeDelete(Path path, ProjectId projectId) {
        try {
            Files.deleteIfExists(path);
            logger.info("{} Deleted temp file {}" , projectId, path);
        } catch (IOException e) {
            logger.warn("{} Failed to delete temp file {}" , projectId, path, e);
        }
    }

    /**
     * Validates that the downloaded blob contains a readable BinaryOWL change log.
     *
     * <p>The method reads the stream using {@link BinaryOWLOntologyChangeLog#readChanges} with a no-op handler;
     * any I/O or parse/runtime error indicates an invalid or unreadable history and results in an exception.</p>
     *
     * @param path         Local filesystem path of the downloaded blob.
     * @param projectId    Project id for log context.
     * @param blobLocation Source blob location for diagnostics.
     * @throws UncheckedIOException     If the file cannot be read from disk.
     * @throws IllegalArgumentException If the file content is not a valid BinaryOWL change log.
     */
    private void checkRevisionHistoryLoads(Path path, ProjectId projectId, BlobLocation blobLocation) {
        logger.info("{} Checking supplied replacement change history is valid before replacing the existing history with it (bucket={} name={})" ,
                projectId, blobLocation.bucket(), blobLocation.name());
        try (var inputStream = new BufferedInputStream(Files.newInputStream(path))) {
            var log = new BinaryOWLOntologyChangeLog();
            // No-op handler; just verifying readability/structure
            log.readChanges(inputStream, dataFactory, (__, ___, ____) -> {
            });
            logger.info("{} Supplied change history IS valid (bucket={} name={})" ,
                    projectId, blobLocation.bucket(), blobLocation.name());
        } catch (IOException e) {
            logger.warn("{} Failed to read change history (bucket={} name={})" ,
                    projectId, blobLocation.bucket(), blobLocation.name(), e);
            throw new UncheckedIOException("Failed to read change history (bucket="
                                           + blobLocation.bucket() + " name=" + blobLocation.name() + ")" , e);
        } catch (RuntimeException e) {
            logger.error("{} Invalid history format (bucket={} name={})" ,
                    projectId, blobLocation.bucket(), blobLocation.name(), e);
            throw new IllegalArgumentException("Invalid history format (bucket="
                                               + blobLocation.bucket() + ", name=" + blobLocation.name() + ")" , e);
        }
    }
}
