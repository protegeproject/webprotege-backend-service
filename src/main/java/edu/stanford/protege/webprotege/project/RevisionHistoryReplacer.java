package edu.stanford.protege.webprotege.project;

import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.revision.ChangeHistoryFileFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

/**
 * Replaces a project's persisted revision/change history file with a supplied file.
 *
 * <p><b>Semantics</b></p>
 * <ul>
 *   <li>Ensures the target directory exists.</li>
 *   <li>Performs a single-step {@code Files.move} with {@code REPLACE_EXISTING} to avoid a delete–move race.</li>
 *   <li>Attempts {@code ATOMIC_MOVE} first; if unsupported (e.g., cross-filesystem), falls back to a regular move.</li>
 *   <li>Runs asynchronously on an injected, bounded {@link Executor}.</li>
 * </ul>
 *
 * <p><b>Concurrency</b></p>
 * <ul>
 *   <li>At most one replacement per {@link ProjectId} may run at a time in this JVM.</li>
 *   <li>Concurrent calls for the same project are <em>rejected</em> with a failed future containing
 *   {@link ConcurrentReplaceInProgressException}.</li>
 *   <li>Calls for different projects proceed in parallel.</li>
 * </ul>
 *
 * <p>Errors surface as {@link UncheckedIOException} (I/O) or {@link ConcurrentReplaceInProgressException} (concurrency rejection).</p>
 */
@Component
public class RevisionHistoryReplacer {

    private static final Logger logger = LoggerFactory.getLogger(RevisionHistoryReplacer.class);

    private final ChangeHistoryFileFactory changeHistoryFileFactory;
    private final Executor ioExecutor;

    /** Running operations keyed by ProjectId; values are the futures owned by the starter call. */
    private final ConcurrentHashMap<ProjectId, CompletableFuture<Void>> inFlight = new ConcurrentHashMap<>();

    /**
     * @param changeHistoryFileFactory Factory providing the canonical project history path.
     * @param ioExecutor               Bounded executor for filesystem work (I/O bound).
     */
    public RevisionHistoryReplacer(ChangeHistoryFileFactory changeHistoryFileFactory,
                                   @Qualifier("projectHistoryIoExecutor") Executor ioExecutor) {
        this.changeHistoryFileFactory = changeHistoryFileFactory;
        this.ioExecutor = ioExecutor;
    }

    /**
     * Replace the project's change history file with the history at the given {@code sourcePath}.
     *
     * <p>If another replacement for the same {@code projectId} is already running, this call is
     * <strong>rejected</strong> and a failed future is returned immediately with
     * {@link ConcurrentReplaceInProgressException}.</p>
     *
     * <p>The returned future completes when the replacement has been persisted. On error,
     * the future completes exceptionally with {@link UncheckedIOException}.</p>
     *
     * @param projectId  The project whose history is being replaced.
     * @param sourcePath The path to the new history file on local disk.
     * @return A future that completes when the move is done, or a failed future if rejected.
     */
    public CompletableFuture<Void> replaceRevisionHistory(ProjectId projectId, Path sourcePath) {
        Objects.requireNonNull(projectId, "projectId must not be null");
        Objects.requireNonNull(sourcePath, "sourcePath must not be null");

        // Install a new gate if none is in-flight; otherwise leave the existing mapping and reject.
        final CompletableFuture<Void> gate = new CompletableFuture<>();
        CompletableFuture<Void> mapped = inFlight.compute(projectId, (k, existing) ->
        {
            if (existing == null || existing.isDone()) {
                return gate;
            }
            return existing;
        });

        if (mapped != gate) {
            logger.info("{} Rejecting concurrent replace request (sourcePath={})", projectId, sourcePath);
            return CompletableFuture.failedFuture(new ConcurrentReplaceInProgressException(projectId));
        }

        // We own execution for this projectId.
        CompletableFuture.runAsync(() -> {
            var targetPath = changeHistoryFileFactory.getChangeHistoryFile(projectId).toPath();
            try {
                logger.info("{} Replacing change history file (sourcePath={} targetPath={})",
                        projectId, sourcePath, targetPath);

                var parent = targetPath.getParent();
                if (parent != null) {
                    Files.createDirectories(parent);
                }

                // Guard against accidental self-move
                if (Files.exists(sourcePath) && Files.exists(targetPath)) {
                    try {
                        if (Files.isSameFile(sourcePath, targetPath)) {
                            logger.info("{} Source and target are the same file; nothing to do (path={})",
                                    projectId, targetPath);
                            return;
                        }
                    } catch (IOException ignored) {
                        // If we can't determine, proceed with move attempt.
                    }
                }

                // Prefer atomic rename when supported (same filesystem)
                try {
                    Files.move(sourcePath, targetPath,
                            StandardCopyOption.REPLACE_EXISTING,
                            StandardCopyOption.ATOMIC_MOVE);
                } catch (AtomicMoveNotSupportedException e) {
                    logger.info("{} ATOMIC_MOVE not supported; falling back to non-atomic move (sourcePath={} targetPath={})",
                            projectId, sourcePath, targetPath);
                    Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                }

                logger.info("{} Moved change history file into place (targetPath={})", projectId, targetPath);
            } catch (IOException e) {
                logger.warn("{} Failed to replace change history (sourcePath={} targetPath={})",
                        projectId, sourcePath, targetPath, e);
                throw new UncheckedIOException(e);
            }
        }, ioExecutor).whenComplete((__, throwable) -> {
            try {
                if (throwable == null) {
                    gate.complete(null);
                } else {
                    gate.completeExceptionally(throwable);
                }
            } finally {
                // Remove only if still mapped to this gate to avoid clobbering a newly started op.
                inFlight.remove(projectId, gate);
            }
        });

        return gate;
    }
}
