package edu.stanford.bmir.protege.web.shared.search;

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
 * 2020-08-17
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("SetSearchSettings")
public abstract class SetSearchSettingsAction implements ProjectAction<SetSearchSettingsResult> {

    @JsonCreator
    public static SetSearchSettingsAction create(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                                 @JsonProperty("from") @Nonnull ImmutableList<EntitySearchFilter> from,
                                                 @JsonProperty("to") @Nonnull ImmutableList<EntitySearchFilter> to) {
        return new AutoValue_SetSearchSettingsAction(projectId, from, to);
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    @Nonnull
    public abstract ImmutableList<EntitySearchFilter> getFrom();

    @Nonnull
    public abstract ImmutableList<EntitySearchFilter> getTo();
}
