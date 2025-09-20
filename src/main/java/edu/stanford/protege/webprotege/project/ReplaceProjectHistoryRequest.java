package edu.stanford.protege.webprotege.project;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.BlobLocation;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.ProjectRequest;

import static edu.stanford.protege.webprotege.project.ReplaceProjectHistoryRequest.CHANNEL;

@JsonTypeName(CHANNEL)
public record ReplaceProjectHistoryRequest(@JsonProperty("projectId") ProjectId projectId,
                                           @JsonProperty("projectHistoryLocation") BlobLocation projectHistoryLocation) implements ProjectRequest<ReplaceProjectHistoryResponse> {

    public static final String CHANNEL = "webprotege.history.ReplaceProjectHistory";

    @Override
    public String getChannel() {
        return CHANNEL;
    }
}
