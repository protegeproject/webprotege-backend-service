package edu.stanford.bmir.protege.web.shared.viz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-14
 */
@AutoValue
@GwtCompatible(serializable = true)
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
