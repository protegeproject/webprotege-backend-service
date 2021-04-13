package edu.stanford.bmir.protege.web.shared.perspective;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.HasProjectId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import jsinterop.annotations.JsIgnore;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.validation.constraints.Null;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18/02/16
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("SetPerspectives")
public abstract class SetPerspectivesAction implements ProjectAction<SetPerspectivesResult>, HasProjectId {


    public static SetPerspectivesAction create(@Nonnull ProjectId projectId,
                                               @Nonnull ImmutableList<PerspectiveDescriptor> perspectiveIds) {
        return new AutoValue_SetPerspectivesAction(projectId, null, perspectiveIds);
    }



    public static SetPerspectivesAction create(@Nonnull ProjectId projectId,
                                               @Nonnull Optional<UserId> userId,
                                               @Nonnull ImmutableList<PerspectiveDescriptor> perspectiveIds) {
        return new AutoValue_SetPerspectivesAction(projectId, userId.orElse(null), perspectiveIds);
    }

    @JsonCreator
    public static SetPerspectivesAction create(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                               @JsonProperty("userId") @Nullable UserId userId,
                                               @JsonProperty("perspectiveDescriptors") @Nonnull ImmutableList<PerspectiveDescriptor> perspectiveIds) {
        return new AutoValue_SetPerspectivesAction(projectId, userId, perspectiveIds);
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    @JsonProperty("userId")
    public abstract UserId getUserIdInternal();

    @JsonIgnore
    public Optional<UserId> getUserId() {
        return Optional.ofNullable(getUserIdInternal());
    }

    @Nonnull
    public abstract ImmutableList<PerspectiveDescriptor> getPerspectiveDescriptors();

}

