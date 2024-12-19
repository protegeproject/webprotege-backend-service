package edu.stanford.protege.webprotege.icd.projects;

import com.fasterxml.jackson.annotation.*;
import edu.stanford.protege.webprotege.common.*;

import static edu.stanford.protege.webprotege.icd.projects.CreateProjectSmallFilesRequest.CHANNEL;

@JsonTypeName(CHANNEL)
public record CreateProjectSmallFilesRequest(
        @JsonProperty("projectId") ProjectId projectId
)implements Request<CreateProjectSmallFilesResponse> {
    public static final String CHANNEL = "icatx.versioning.CreateProjectSmallFiles";

    @Override
    public String getChannel() {
        return CHANNEL;
    }
}
