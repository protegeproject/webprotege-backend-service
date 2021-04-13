package edu.stanford.bmir.protege.web.shared.viz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-10
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("SetUserProjectEntityGraphSettings")
public abstract class SetUserProjectEntityGraphSettingsAction implements ProjectAction<SetUserProjectEntityGraphSettingsResult> {

    @JsonCreator
    public static SetUserProjectEntityGraphSettingsAction create(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                                                 @JsonProperty("userId") @Nullable UserId userId,
                                                                 @JsonProperty("settings") @Nonnull EntityGraphSettings settings) {
        return new AutoValue_SetUserProjectEntityGraphSettingsAction(projectId, settings, userId);
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    @Nonnull
    public abstract EntityGraphSettings getSettings();

    @Nonnull
    public Optional<UserId> getUserId() {
        return Optional.ofNullable(getUserIdInternal());
    }

    @Nullable
    public abstract UserId getUserIdInternal();
}
