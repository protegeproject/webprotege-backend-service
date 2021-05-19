package edu.stanford.protege.webprotege.repository;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.search.EntitySearchFilter;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-15
 */
public interface ProjectEntitySearchFiltersManager {

    @Nonnull
    ImmutableList<EntitySearchFilter> getSearchFilters();

    void setSearchFilters(@Nonnull ImmutableList<EntitySearchFilter> searchFilters);
}
