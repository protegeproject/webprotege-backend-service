package edu.stanford.protege.webprotege.shortform;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.match.EntityMatcherFactory;
import edu.stanford.protege.webprotege.repository.ProjectEntitySearchFiltersManager;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-21
 */
public class EntitySearchFilterMatchersFactory {

    @Nonnull
    private final ProjectEntitySearchFiltersManager filtersManager;

    @Nonnull
    private final EntityMatcherFactory entityMatcherFactory;

    @Inject
    public EntitySearchFilterMatchersFactory(@Nonnull ProjectEntitySearchFiltersManager filtersManager,
                                             @Nonnull EntityMatcherFactory entityMatcherFactory) {
        this.filtersManager = checkNotNull(filtersManager);
        this.entityMatcherFactory = checkNotNull(entityMatcherFactory);
    }

    @Nonnull
    public ImmutableList<EntitySearchFilterMatcher> getSearchFilterMatchers() {
        return filtersManager.getSearchFilters()
                      .stream()
                      .map(filter -> {
                          var matcher = entityMatcherFactory.getEntityMatcher(filter.getEntityMatchCriteria());
                          return EntitySearchFilterMatcher.get(filter, matcher);
                      })
                      .collect(toImmutableList());
    }
}
