package edu.stanford.protege.webprotege.index.impl;

import edu.stanford.protege.webprotege.index.IndexUpdatingService;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.revision.RevisionManager;

import javax.annotation.Nonnull;
import java.util.Set;
import java.util.concurrent.ExecutorService;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-07-13
 */
public class IndexUpdaterFactory {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final RevisionManager revisionManager;

    @Nonnull
    private final Set<UpdatableIndex> indexes;

    @Nonnull @IndexUpdatingService
    private final ExecutorService indexUpdaterService;

    public IndexUpdaterFactory(@Nonnull ProjectId projectId,
                               @Nonnull RevisionManager revisionManager,
                               @Nonnull Set<UpdatableIndex> indexes,
                               @Nonnull ExecutorService indexUpdaterService) {
        this.projectId = projectId;
        this.revisionManager = revisionManager;
        this.indexes = indexes;
        this.indexUpdaterService = indexUpdaterService;
    }

    public IndexUpdater create() {
        return new IndexUpdater(revisionManager, indexes, indexUpdaterService, projectId);
    }
}
