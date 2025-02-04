package edu.stanford.protege.webprotege.icd.projects;

import com.fasterxml.jackson.annotation.*;
import edu.stanford.protege.webprotege.common.*;

import static edu.stanford.protege.webprotege.icd.projects.CreateNewReproducibleProjectRequest.CHANNEL;

@JsonTypeName(CHANNEL)
public record CreateNewReproducibleProjectRequest(
        @JsonProperty("projectId") ProjectId projectId,
        @JsonProperty("branch") String branch
)implements Request<CreateNewReproducibleProjectResponse> {
    public static final String CHANNEL = "icatx.versioning.CreateNewReproducibleProjectRequest";

    @Override
    public String getChannel() {
        return CHANNEL;
    }
}
