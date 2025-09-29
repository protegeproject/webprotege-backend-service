package edu.stanford.protege.webprotege.project;

import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.hierarchy.HierarchyProviderManager;
import edu.stanford.protege.webprotege.index.impl.IndexUpdater;
import edu.stanford.protege.webprotege.project.chg.ChangeManager;
import edu.stanford.protege.webprotege.revision.ReloadableRevisionStore;
import edu.stanford.protege.webprotege.shortform.LuceneIndexWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public class ProjectHistoryReplacer {

    private static final Logger logger = LoggerFactory.getLogger(ProjectHistoryReplacer.class);

    private final ChangeManager changeManager;

    private final ProjectId projectId;

    private final RevisionHistoryReplacer revisionHistoryReplacer;

    private final IndexUpdater indexUpdater;

    private final ReloadableRevisionStore revisionStore;

    private final LuceneIndexWriter luceneIndexWriter;

    private final HierarchyProviderManager hierarchyProviderManager;

    public ProjectHistoryReplacer(ChangeManager changeManager,
                                  ProjectId projectId,
                                  RevisionHistoryReplacer revisionHistoryReplacer,
                                  IndexUpdater indexUpdater,
                                  ReloadableRevisionStore revisionStore,
                                  LuceneIndexWriter luceneIndexWriter,
                                  HierarchyProviderManager hierarchyProviderManager) {
        this.changeManager = changeManager;
        this.projectId = projectId;
        this.revisionHistoryReplacer = revisionHistoryReplacer;
        this.indexUpdater = indexUpdater;
        this.revisionStore = revisionStore;
        this.luceneIndexWriter = luceneIndexWriter;
        this.hierarchyProviderManager = hierarchyProviderManager;
    }

    public synchronized CompletableFuture<Void> replaceProjectHistory(Path replaceWith,
                                                                      ProjectHistoryReplacementOperationId opId) {
        var freeze = changeManager.freezeWrites();
        logger.info("{} Froze project for writes [opId={}]" , projectId, opId);

        // B: replace history → C: reload & rebuild (async on maintenance executor)
        CompletableFuture<Void> replaceAndRebuild =
                revisionHistoryReplacer.replaceRevisionHistory(projectId, replaceWith)
                        .thenRunAsync(() -> reloadRevisionStoreAndRebuildIndexes(opId));

        // Unfreeze after B+C regardless of outcome; don’t mask upstream failures
        return replaceAndRebuild.whenComplete((v, ex) -> safeUnfreeze(freeze));
    }

    private void reloadRevisionStoreAndRebuildIndexes(ProjectHistoryReplacementOperationId opId) {
        try {
            logger.info("[{}] Reloading revision store [opId={}]" , projectId, opId);
            revisionStore.reload();
            logger.info("[{}] Rebuilding project indexes [opId={}]" , projectId, opId);
            indexUpdater.rebuildIndexes();
            logger.info("[{}] Rebuilding Lucene indexes [opId={}]" , projectId, opId);
            luceneIndexWriter.rebuildIndex();
            logger.info("[{}] Resetting hierarchy providers [opId={}]" , projectId, opId);
            hierarchyProviderManager.resetHierarchyProviders();
            logger.info("[{}] Finished maintenance tasks [opId={}]" , projectId, opId);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void safeUnfreeze(ProjectFreezeHandle freeze) {
        try {
            freeze.unfreeze();
            logger.info("[{}] Unfroze project for writes" , projectId);
        } catch (Exception e) {
            logger.error("[{}] Failed to unfreeze project for writes" , projectId, e);
        }
    }
}
