package edu.stanford.protege.webprotege.project;

import edu.stanford.protege.webprotege.common.BlobLocation;
import edu.stanford.protege.webprotege.common.EventId;
import edu.stanford.protege.webprotege.common.ProjectEvent;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.event.LargeNumberOfChangesEvent;
import edu.stanford.protege.webprotege.ipc.EventDispatcher;
import edu.stanford.protege.webprotege.storage.MinioFileDownloader;
import org.semanticweb.binaryowl.BinaryOWLOntologyChangeLog;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.Nonnull;
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
 *   <li>Generate a correlation {@link ProjectHistoryReplacementOperationId} if the request is accepted.</li>
 *   <li>Download the supplied blob to a local temp file.</li>
 *   <li>Validate the blob is a readable BinaryOWL change log.</li>
 *   <li>Replace the project's stored history.</li>
 *   <li><strong>Attempt</strong> to delete the temp file (on success or failure). Deletion is best-effort; a failure to
 *       delete does <em>not</em> affect the outcome of the replace and is only logged for operator visibility.</li>
 *   <li>On success, dispatch:
 *     <ul>
 *       <li>a {@link ProjectHistoryReplacementSucceededEvent} (explicit domain event), and</li>
 *       <li>a packaged {@link LargeNumberOfChangesEvent} so clients refresh.</li>
 *     </ul>
 *     Both events reuse the same correlation {@link ProjectHistoryReplacementOperationId} returned in the response.
 *   </li>
 *   <li>On failure, dispatch a {@link ProjectHistoryReplacementFailedEvent} with details (no packaged refresh event).</li>
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
 *       {@link ProjectHistoryReplacementOperationId}. This id is reused by dispatched events so clients can
 *       correlate ACK → event(s).</li>
 *   <li>On rejection due to an in-flight operation, an immediate response is returned with status
 *       {@code REJECTED_ALREADY_IN_PROGRESS} and a {@code null} correlation id (no event will be dispatched).</li>
 * </ul>
 */
public class ProjectHistoryReplacementSagaImpl implements ProjectHistoryReplacementSaga {

    private static final Logger logger = LoggerFactory.getLogger(ProjectHistoryReplacementSaga.class);

    private final AtomicBoolean inProgress = new AtomicBoolean(false);

    @Nonnull
    private final ProjectHistoryReplacer projectHistoryReplacer;

    @Nonnull
    private final MinioFileDownloader minioFileDownloader;

    @Nonnull
    private final EventDispatcher eventDispatcher;

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Nonnull
    private final Executor replaceProjectHistoryExecutor;

    public ProjectHistoryReplacementSagaImpl(@Nonnull ProjectHistoryReplacer projectHistoryReplacer,
                                             @Nonnull MinioFileDownloader minioFileDownloader,
                                             @Nonnull EventDispatcher eventDispatcher,
                                             @Nonnull OWLDataFactory dataFactory,
                                             @Nonnull @Qualifier("replaceProjectHistoryExecutor" ) Executor replaceProjectHistoryExecutor) {
        this.projectHistoryReplacer = projectHistoryReplacer;
        this.minioFileDownloader = minioFileDownloader;
        this.eventDispatcher = eventDispatcher;
        this.dataFactory = dataFactory;
        this.replaceProjectHistoryExecutor = replaceProjectHistoryExecutor;
    }

    /**
     * Unwrap Completion/Execution wrappers so logs and event messages show the real cause.
     */
    @Nonnull
    private static Throwable rootCause(@Nonnull Throwable t) {
        if (t instanceof java.util.concurrent.CompletionException ce && ce.getCause() != null) {
            return ce.getCause();
        }
        if (t instanceof java.util.concurrent.ExecutionException ee && ee.getCause() != null) {
            return ee.getCause();
        }
        return t;
    }

    @Override
    @Nonnull
    public ReplaceProjectHistoryResponse run(@Nonnull ReplaceProjectHistoryRequest request) {
        var projectId = request.projectId();
        var blobLocation = request.projectHistoryLocation();

        if (!inProgress.compareAndSet(false, true)) {
            logger.info("{} Replace already in progress; ignoring new request (bucket={} name={})" ,
                    projectId, blobLocation.bucket(), blobLocation.name());
            return new ReplaceProjectHistoryResponse(
                    projectId,
                    ReplaceProjectHistoryRequestStatus.REJECTED_ALREADY_IN_PROGRESS,
                    null
            );
        }

        var opId = ProjectHistoryReplacementOperationId.generate();

        eventDispatcher.dispatchEvent(new ProjectHistoryReplacementStartedEvent(EventId.generate(), opId, projectId));

        logger.info("{} Starting replace of project history (bucket={} name={}) [opId={}]" ,
                projectId, blobLocation.bucket(), blobLocation.name(), opId);

        minioFileDownloader
                .downloadFile(blobLocation)
                .thenComposeAsync(path -> {
                    try {
                        checkRevisionHistoryLoads(path, projectId, blobLocation, opId);
                        logger.info("{} Downloaded new project history to {} (bucket={} name={}) [opId={}]" ,
                                projectId, path, blobLocation.bucket(), blobLocation.name(), opId);
                        return projectHistoryReplacer.replaceProjectHistory(path, opId)
                                .whenComplete((__, ___) -> safeDelete(path, projectId, opId));
                    } catch (Throwable t) {
                        safeDelete(path, projectId, opId);
                        return CompletableFuture.failedFuture(t);
                    }
                }, replaceProjectHistoryExecutor)
                .thenRunAsync(() -> {
                    fireReplaceSucceeded(opId, projectId, blobLocation);
                    fireProjectChangedEvent(opId, projectId);
                }, replaceProjectHistoryExecutor)
                .whenCompleteAsync((result, error) -> inProgress.set(false), replaceProjectHistoryExecutor)
                .exceptionally(ex -> {
                    var cause = rootCause(ex);
                    fireReplaceFailed(opId, projectId, blobLocation, cause);
                    logger.error("{} Replace project history failed (bucket={} name={}) [opId={}]" ,
                            projectId, blobLocation.bucket(), blobLocation.name(), opId, cause);
                    return null;
                });

        return new ReplaceProjectHistoryResponse(
                projectId,
                ReplaceProjectHistoryRequestStatus.ACCEPTED,
                opId
        );
    }

    private void fireReplaceSucceeded(@Nonnull ProjectHistoryReplacementOperationId operationId,
                                      @Nonnull ProjectId projectId,
                                      @Nonnull BlobLocation blobLocation) {
        eventDispatcher.dispatchEvent(
                new ProjectHistoryReplacementSucceededEvent(EventId.generate(), operationId, projectId, blobLocation)
        );
        logger.info("{} Replace project history succeeded [opId={}]" , projectId, operationId);
    }

    private void fireReplaceFailed(@Nonnull ProjectHistoryReplacementOperationId operationId,
                                   @Nonnull ProjectId projectId,
                                   @Nonnull BlobLocation blobLocation,
                                   @Nonnull Throwable cause) {
        eventDispatcher.dispatchEvent(
                new ProjectHistoryReplacementFailedEvent(
                        EventId.generate(), operationId, projectId, cause.getMessage() == null ? "" : cause.getMessage()
                )
        );
        logger.info("{} Replace project history failed for blob (bucket={} name={}) [opId={}]" ,
                projectId, blobLocation.bucket(), blobLocation.name(), operationId);
    }

    private void fireProjectChangedEvent(@Nonnull ProjectHistoryReplacementOperationId operationId,
                                         @Nonnull ProjectId projectId) {
        var eventId = EventId.generate();
        logger.info("{} Firing project changed event [eventId={} opId={}]" , projectId, eventId, operationId);
        var events = List.<ProjectEvent>of(new LargeNumberOfChangesEvent(eventId, projectId));
        var packaged = new PackagedProjectChangeEvent(projectId, eventId, events);
        eventDispatcher.dispatchEvent(packaged);
    }

    /**
     * Best-effort cleanup of the temporary file. A failure to delete does <em>not</em>
     * affect the outcome of the replace; it is only logged for operator visibility.
     */
    private void safeDelete(@Nonnull Path path,
                            @Nonnull ProjectId projectId,
                            @Nonnull ProjectHistoryReplacementOperationId opId) {
        try {
            Files.deleteIfExists(path);
            logger.info("{} Deleted temp file {} [opId={}]" , projectId, path, opId);
        } catch (IOException e) {
            logger.warn("{} Could not delete temp file {} (ignored) [opId={}]" , projectId, path, opId, e);
        }
    }

    private void checkRevisionHistoryLoads(@Nonnull Path path,
                                           @Nonnull ProjectId projectId,
                                           @Nonnull BlobLocation blobLocation,
                                           @Nonnull ProjectHistoryReplacementOperationId opId) {
        logger.info("{} Checking supplied replacement change history is valid (bucket={} name={}) [opId={}]" ,
                projectId, blobLocation.bucket(), blobLocation.name(), opId);
        try (var inputStream = new BufferedInputStream(Files.newInputStream(path))) {
            var log = new BinaryOWLOntologyChangeLog();
            log.readChanges(inputStream, dataFactory, (__, ___, ____) -> {
            });
            logger.info("{} Supplied change history IS valid (bucket={} name={}) [opId={}]" ,
                    projectId, blobLocation.bucket(), blobLocation.name(), opId);
        } catch (IOException e) {
            logger.warn("{} Failed to read change history (bucket={} name={}) [opId={}]" ,
                    projectId, blobLocation.bucket(), blobLocation.name(), opId, e);
            throw new UncheckedIOException("Failed to read change history (bucket="
                                           + blobLocation.bucket() + " name=" + blobLocation.name() + ")" , e);
        } catch (RuntimeException e) {
            logger.error("{} Invalid history format (bucket={} name={}) [opId={}]" ,
                    projectId, blobLocation.bucket(), blobLocation.name(), opId, e);
            throw new IllegalArgumentException("Invalid history format (bucket="
                                               + blobLocation.bucket() + ", name=" + blobLocation.name() + ")" , e);
        }
    }
}
