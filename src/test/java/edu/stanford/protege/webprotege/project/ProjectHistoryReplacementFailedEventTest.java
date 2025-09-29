package edu.stanford.protege.webprotege.project;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.protege.webprotege.common.EventId;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProjectHistoryReplacementFailedEventTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldCreateEventAndReturnValues() {
        var eventId = EventId.generate();
        var operationId = ProjectHistoryReplacementOperationId.generate();
        var projectId = ProjectId.generate();
        var message = "Replacement failed due to conflict";

        var event = new ProjectHistoryReplacementFailedEvent(eventId, operationId, projectId, message);

        assertThat(event.eventId()).isEqualTo(eventId);
        assertThat(event.operationId()).isEqualTo(operationId);
        assertThat(event.projectId()).isEqualTo(projectId);
        assertThat(event.message()).isEqualTo(message);
    }

    @Test
    void shouldReturnChannelConstant() {
        var event = new ProjectHistoryReplacementFailedEvent(
                EventId.generate(),
                ProjectHistoryReplacementOperationId.generate(),
                ProjectId.generate(),
                "Failure message"
        );

        assertThat(event.getChannel())
                .isEqualTo(ProjectHistoryReplacementFailedEvent.CHANNEL);
    }

    @Test
    void shouldImplementEquality() {
        var eventId = EventId.generate();
        var operationId = ProjectHistoryReplacementOperationId.generate();
        var projectId = ProjectId.generate();
        var message = "Replacement failed due to I/O error";

        var event1 = new ProjectHistoryReplacementFailedEvent(eventId, operationId, projectId, message);
        var event2 = new ProjectHistoryReplacementFailedEvent(eventId, operationId, projectId, message);

        assertThat(event1).isEqualTo(event2);
        assertThat(event1.hashCode()).isEqualTo(event2.hashCode());
    }

    @Test
    void shouldSerializeAndDeserialize() throws JsonProcessingException {
        var eventId = EventId.generate();
        var operationId = ProjectHistoryReplacementOperationId.generate();
        var projectId = ProjectId.generate();
        var message = "Replacement failed due to network error";

        var event = new ProjectHistoryReplacementFailedEvent(eventId, operationId, projectId, message);

        var json = objectMapper.writeValueAsString(event);

        assertThat(json)
                .contains("\"eventId\"")
                .contains("\"operationId\"")
                .contains("\"projectId\"")
                .contains("\"message\"");

        var roundTripped = objectMapper.readValue(json, ProjectHistoryReplacementFailedEvent.class);

        assertThat(roundTripped).isEqualTo(event);
    }
}
