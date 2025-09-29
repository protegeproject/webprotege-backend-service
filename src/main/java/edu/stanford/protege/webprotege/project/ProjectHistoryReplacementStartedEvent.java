package edu.stanford.protege.webprotege.project;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.EventId;
import edu.stanford.protege.webprotege.common.ProjectId;

import static edu.stanford.protege.webprotege.project.ProjectHistoryReplacementStartedEvent.CHANNEL;

@JsonTypeName(CHANNEL)
public record ProjectHistoryReplacementStartedEvent(@JsonProperty("eventId") EventId eventId,
                                                   @JsonProperty("operationId") ProjectHistoryReplacementOperationId operationId,
                                                   @JsonProperty("projectId") ProjectId projectId) implements ProjectHistoryReplacementEvent {

    public static final String CHANNEL = "webprotege.events.projects.ProjectHistoryReplacementStarted";

    @Override
    public String getChannel() {
        return CHANNEL;
    }
}
