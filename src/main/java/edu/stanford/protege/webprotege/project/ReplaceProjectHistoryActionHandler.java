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
 *   <li>Download the supplied blob to a local temp file.</li>
 *   <li>Validate the blob is a readable BinaryOWL change log.</li>
 *   <li>Replace the project's stored history.</li>
 *   <li>Delete the temp file.</li>
 *   <li>Dispatch a {@link LargeNumberOfChangesEvent} to notify listeners.</li>
 * </ol>
 *
 * <p><strong>Concurrency</strong></p>
 * <ul>
 *   <li>This handler instance is scoped to a single {@link ProjectId} and uses a lightweight, in-process
 *   guard to prevent overlapping replace operations for that project within the same JVM.</li>
 *   <li>When a request arrives while another replace is in progress, it is <em>not</em> started and the response
 *   carries a status indicating the request was rejected (e.g., {@code REJECTED_ALREADY_IN_PROGRESS}).</li>
 *   <li>The guard only applies within the current JVM process; cross-node coordination (if running multiple app
 *   instances) is out of scope here.</li>
 * </ul>
 *
 * <p><strong>Response semantics</strong></p>
 * <ul>
 *   <li>On acceptance, an immediate response is returned with status {@code ACCEPTED} while the work continues
 *   asynchronously; success/failure is reflected in logs and by subsequent event dispatch on success.</li>
 *   <li>On rejection due to an in-flight operation, an immediate response is returned with status
 *   {@code REJECTED_ALREADY_IN_PROGRESS} (or equivalent), and no work is started.</li>
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
     * @param eventDispatcher               Event bus used to notify subscribers after a successful replace.
     * @param dataFactory                   OWL data factory used to validate the BinaryOWL stream.
     * @param replaceProjectHistoryExecutor Bounded executor on which the async pipeline runs.
     */
    public ReplaceProjectHistoryActionHandler(@NotNull AccessManager accessManager,
                                              ProjectHistoryReplacer projectHistoryReplacer,
                                              MinioFileDownloader minioFileDownloader,
                                              EventDispatcher eventDispatcher,
                                              OWLDataFactory dataFactory,
                                              @Qualifier("replaceProjectHistoryExecutor") Executor replaceProjectHistoryExecutor) {
        super(accessManager);
        this.projectHistoryReplacer = projectHistoryReplacer;
        this.minioFileDownloader = minioFileDownloader;
        this.eventDispatcher = eventDispatcher;
        this.dataFactory = dataFactory;
        this.replaceProjectHistoryExecutor = replaceProjectHistoryExecutor;
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public Class<ReplaceProjectHistoryRequest> getActionClass() {
        return ReplaceProjectHistoryRequest.class;
    }

    /**
     * Starts an asynchronous replace of the project's revision history, returning immediately with an
     * {@link ReplaceProjectHistoryResponse} that indicates whether the request was accepted for processing.
     *
     * <p>If a replace is already in progress for this handler's {@link ProjectId}, the request is rejected and
     * no work is started.</p>
     *
     * @param action           The request containing the {@link ProjectId} and the {@link BlobLocation}.
     * @param executionContext The execution context for the request.
     * @return A response with:
     * <ul>
     *   <li>{@code ACCEPTED} when the replace has been scheduled asynchronously,</li>
     *   <li>{@code REJECTED_ALREADY_IN_PROGRESS} (or equivalent) when another replace is currently running.</li>
     * </ul>
     * Subsequent success/failure (for accepted requests) is reported via logs and, on success,
     * by dispatching a {@link LargeNumberOfChangesEvent}.
     */
    @NotNull
    @Override
    public ReplaceProjectHistoryResponse execute(@NotNull ReplaceProjectHistoryRequest action,
                                                 @NotNull ExecutionContext executionContext) {
        var projectId = action.projectId();
        var blobLocation = action.projectHistoryLocation();

        // Fast, non-blocking concurrency guard per handler instance
        if (!inProgress.compareAndSet(false, true)) {
            logger.info("{} Replace already in progress; ignoring new request (bucket={} name={})",
                    projectId, blobLocation.bucket(), blobLocation.name());
            return new ReplaceProjectHistoryResponse(projectId, ReplaceProjectHistoryRequestStatus.REJECTED_ALREADY_IN_PROGRESS, null);
        }

        logger.info("{} Replacing project history (bucket={} name={})",
                projectId, blobLocation.bucket(), blobLocation.name());

        var eventId = EventId.generate();

        minioFileDownloader
                .downloadFile(blobLocation)
                .thenComposeAsync(path -> {
                    try {
                        checkRevisionHistoryLoads(path, projectId, blobLocation);
                        logger.info("{} Downloaded new project history to {} (bucket={} name={})",
                                projectId, path, blobLocation.bucket(), blobLocation.name());
                        return projectHistoryReplacer.replaceProjectHistory(path)
                                .whenComplete((__, ___) -> safeDelete(path, projectId));
                    } catch (Throwable t) {
                        // Validation failed: make sure we don't leak the file
                        safeDelete(path, projectId);
                        return CompletableFuture.failedFuture(t);
                    }
                }, replaceProjectHistoryExecutor)
                .thenRunAsync(() -> fireProjectChangedEvent(projectId, eventId), replaceProjectHistoryExecutor)
                .whenComplete((__, ___) -> inProgress.set(false)) // ALWAYS release the guard
                .exceptionally(ex -> {
                    logger.error("{} Replace project history failed (bucket={} name={})",
                            projectId, blobLocation.bucket(), blobLocation.name(), ex);
                    return null;
                });

        // Immediate ACK – operation continues asynchronously
        return new ReplaceProjectHistoryResponse(projectId, ReplaceProjectHistoryRequestStatus.ACCEPTED, eventId);
    }

    /**
     * Dispatches a {@link LargeNumberOfChangesEvent} packaged for the given project.
     * Called after the history has been successfully replaced.
     *
     * @param projectId The project whose clients should be notified.
     */
    private void fireProjectChangedEvent(ProjectId projectId, EventId eventId) {
        logger.info("{} Firing project changed event", projectId);
        var events = List.<ProjectEvent>of(
                new LargeNumberOfChangesEvent(eventId, projectId)
        );
        var packaged = new PackagedProjectChangeEvent(projectId, eventId, events);
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
            logger.info("{} Deleted temp file {}", projectId, path);
        } catch (IOException e) {
            logger.warn("{} Failed to delete temp file {}", projectId, path, e);
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
     * @throws UncheckedIOException   If the file cannot be read from disk.
     * @throws IllegalArgumentException If the file content is not a valid BinaryOWL change log.
     */
    private void checkRevisionHistoryLoads(Path path, ProjectId projectId, BlobLocation blobLocation) {
        logger.info("{} Checking supplied replacement change history is valid before replacing the existing history with it (bucket={} name={})",
                projectId, blobLocation.bucket(), blobLocation.name());
        try (var inputStream = new BufferedInputStream(Files.newInputStream(path))) {
            var log = new BinaryOWLOntologyChangeLog();
            // No-op handler; just verifying readability/structure
            log.readChanges(inputStream, dataFactory, (__, ___, ____) -> {});
            logger.info("{} Supplied change history IS valid (bucket={} name={})",
                    projectId, blobLocation.bucket(), blobLocation.name());
        } catch (IOException e) {
            logger.warn("{} Failed to read change history (bucket={} name={})",
                    projectId, blobLocation.bucket(), blobLocation.name(), e);
            throw new UncheckedIOException("Failed to read change history (bucket="
                                           + blobLocation.bucket() + " name=" + blobLocation.name() + ")", e);
        } catch (RuntimeException e) {
            logger.error("{} Invalid history format (bucket={} name={})",
                    projectId, blobLocation.bucket(), blobLocation.name(), e);
            throw new IllegalArgumentException("Invalid history format (bucket="
                                               + blobLocation.bucket() + ", name=" + blobLocation.name() + ")", e);
        }
    }
}
