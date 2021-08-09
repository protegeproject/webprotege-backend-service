package edu.stanford.protege.webprotege.viz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.project.HasProjectId;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.user.UserId;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-11
 */
@AutoValue

public abstract class ProjectUserEntityGraphSettings implements HasProjectId {

    public static final String PROJECT_ID = "projectId";

    public static final String USER_ID = "userId";

    private static final String SETTINGS = "settings";

    @JsonCreator
    public static ProjectUserEntityGraphSettings get(@Nonnull @JsonProperty(PROJECT_ID) ProjectId projectId,
                                                     @Nullable @JsonProperty(USER_ID) UserId userId,
                                                     @Nonnull @JsonProperty(SETTINGS) EntityGraphSettings settings) {
        return new AutoValue_ProjectUserEntityGraphSettings(projectId, userId, settings);
    }

    @Nonnull
    public static ProjectUserEntityGraphSettings getDefault(@Nonnull ProjectId projectId,
                                                            @Nullable UserId userId) {
        return get(projectId, userId, EntityGraphSettings.getDefault());
    }

    @JsonProperty(PROJECT_ID)
    @Nonnull
    public abstract ProjectId getProjectId();

    @JsonProperty(USER_ID)
    @Nullable
    public abstract UserId getUserIdInternal();

    @JsonProperty(SETTINGS)
    @Nonnull
    public abstract EntityGraphSettings getSettings();

    @JsonIgnore
    @Nullable
    public Optional<UserId> getUserId() {
        return Optional.ofNullable(getUserIdInternal());
    }
}
