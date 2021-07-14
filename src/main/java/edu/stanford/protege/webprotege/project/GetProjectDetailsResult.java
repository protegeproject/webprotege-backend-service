package edu.stanford.protege.webprotege.project;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.Result;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10/03/16
 */
@AutoValue

@JsonTypeName("GetProjectDetails")
public abstract class GetProjectDetailsResult implements Result {

    @JsonCreator
    public static GetProjectDetailsResult get(@JsonProperty("projectDetails") @Nonnull ProjectDetails projectDetails) {
        return new AutoValue_GetProjectDetailsResult(projectDetails);
    }

    @Nonnull
    public abstract ProjectDetails getProjectDetails();
}
