package edu.stanford.protege.webprotege.viz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.common.ProjectId;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-14
 */
@AutoValue

@JsonTypeName("SetEntityGraphActiveFilters")
public abstract class SetEntityGraphActiveFiltersAction implements ProjectAction<SetEntityGraphActiveFiltersResult> {


    @JsonCreator
    public static SetEntityGraphActiveFiltersAction create(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                                           @JsonProperty("activeFilters") @Nonnull ImmutableList<FilterName> activeFilters) {
        return new AutoValue_SetEntityGraphActiveFiltersAction(projectId, activeFilters);
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    @Nonnull
    public abstract ImmutableList<FilterName> getActiveFilters();
}
