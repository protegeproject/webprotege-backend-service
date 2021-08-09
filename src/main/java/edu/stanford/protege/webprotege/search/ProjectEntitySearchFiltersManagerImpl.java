package edu.stanford.protege.webprotege.search;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.repository.ProjectEntitySearchFiltersManager;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-21
 */
public class ProjectEntitySearchFiltersManagerImpl implements ProjectEntitySearchFiltersManager {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final EntitySearchFilterRepository repository;

    @Nonnull
    private final Provider<EntitySearchFilterIndexesManager> indexesManagerProvider;

    @Inject
    public ProjectEntitySearchFiltersManagerImpl(@Nonnull ProjectId projectId,
                                                 @Nonnull EntitySearchFilterRepository repository,
                                                 @Nonnull Provider<EntitySearchFilterIndexesManager> indexesManagerProvider) {
        this.projectId = checkNotNull(projectId);
        this.repository = checkNotNull(repository);
        this.indexesManagerProvider = checkNotNull(indexesManagerProvider);
    }

    @Nonnull
    @Override
    public ImmutableList<EntitySearchFilter> getSearchFilters() {
        return repository.getSearchFilters(projectId);
    }

    @Override
    public void setSearchFilters(@Nonnull ImmutableList<EntitySearchFilter> searchFilters) {
        repository.saveSearchFilters(searchFilters);
        indexesManagerProvider.get().updateEntitySearchFilterIndexes();
    }
}
