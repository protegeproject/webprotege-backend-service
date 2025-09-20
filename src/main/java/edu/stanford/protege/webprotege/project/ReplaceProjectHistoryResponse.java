package edu.stanford.protege.webprotege.project;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.EventId;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.Response;

import javax.annotation.Nullable;

@JsonTypeName(ReplaceProjectHistoryRequest.CHANNEL)
public record ReplaceProjectHistoryResponse(@JsonProperty("projectId") ProjectId projectId,
                                            @JsonProperty("requestStatus") ReplaceProjectHistoryRequestStatus requestStatus,
                                            @JsonProperty("eventId") @Nullable EventId eventId) implements Response, HasProjectId {

}
