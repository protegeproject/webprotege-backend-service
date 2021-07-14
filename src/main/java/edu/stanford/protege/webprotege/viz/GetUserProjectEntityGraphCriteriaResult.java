package edu.stanford.protege.webprotege.viz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.project.ProjectId;
import edu.stanford.protege.webprotege.user.UserId;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-10
 */
@AutoValue

@JsonTypeName("GetUserProjectEntityGraphCriteria")
public abstract class GetUserProjectEntityGraphCriteriaResult implements Result {

    @JsonCreator
    public static GetUserProjectEntityGraphCriteriaResult create(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                                                 @JsonProperty("userId") @Nonnull UserId userId,
                                                                 @JsonProperty("settings") @Nonnull EntityGraphSettings settings) {
        return new AutoValue_GetUserProjectEntityGraphCriteriaResult(projectId,
                                                                     userId,
                                                                     settings);
    }

    @Nonnull
    public abstract ProjectId getProjectId();

    @Nonnull
    public abstract UserId getUserId();

    @Nonnull
    public abstract EntityGraphSettings getSettings();

}
