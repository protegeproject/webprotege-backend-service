package edu.stanford.protege.webprotege.viz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.user.UserId;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-10
 */
@AutoValue

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
