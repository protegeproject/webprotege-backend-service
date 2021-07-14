package edu.stanford.protege.webprotege.project;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.HasUserId;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.user.UserId;

import javax.annotation.Nonnull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/04/2013
 */
@AutoValue

@JsonTypeName("LoadProject")
public abstract class LoadProjectResult implements Result, HasUserId, HasProjectId {

    @JsonCreator
    public static LoadProjectResult get(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                        @JsonProperty("userId") @Nonnull UserId loadedBy,
                                        @JsonProperty("projectDetails") @Nonnull ProjectDetails projectDetails) {
        return new AutoValue_LoadProjectResult(projectId,
                                               loadedBy,
                                               projectDetails);
    }

    @Nonnull
    public abstract ProjectId getProjectId();

    @Override
    public abstract UserId getUserId();

    @Nonnull
    public abstract ProjectDetails getProjectDetails();
}
