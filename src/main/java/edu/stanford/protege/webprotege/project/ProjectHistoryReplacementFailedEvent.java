package edu.stanford.protege.webprotege.project;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.stanford.protege.webprotege.common.EventId;
import edu.stanford.protege.webprotege.common.ProjectId;

public record ProjectHistoryReplacementFailedEvent(@JsonProperty("eventId") EventId eventId,
                                                   @JsonProperty("operationId") ProjectHistoryReplacementOperationId operationId,
                                                   @JsonProperty("projectId") ProjectId projectId,
                                                   @JsonProperty("message") String message) implements ProjectHistoryReplacementCompletionEvent {

    public static final String CHANNEL = "webprotege.events.projects.ProjectHistoryReplacementFailed";

    @Override
    public String getChannel() {
        return CHANNEL;
    }
}
