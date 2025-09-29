package edu.stanford.protege.webprotege.project;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.protege.webprotege.common.BlobLocation;
import edu.stanford.protege.webprotege.common.EventId;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProjectHistoryReplacementSucceededEventTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldCreateEventAndReturnValues() {
        var eventId = EventId.generate();
        var operationId = ProjectHistoryReplacementOperationId.generate();
        var projectId = ProjectId.generate();
        var blobLocation = new BlobLocation("some-bucket", "some-name");

        var event = new ProjectHistoryReplacementSucceededEvent(eventId, operationId, projectId, blobLocation);

        assertThat(event.eventId()).isEqualTo(eventId);
        assertThat(event.operationId()).isEqualTo(operationId);
        assertThat(event.projectId()).isEqualTo(projectId);
        assertThat(event.blobLocation()).isEqualTo(blobLocation);
    }

    @Test
    void shouldReturnChannelConstant() {
        var event = new ProjectHistoryReplacementSucceededEvent(
                EventId.generate(),
                ProjectHistoryReplacementOperationId.generate(),
                ProjectId.generate(),
                new BlobLocation("some-bucket", "some-name")
        );

        assertThat(event.getChannel())
                .isEqualTo(ProjectHistoryReplacementSucceededEvent.CHANNEL);
    }

    @Test
    void shouldImplementEquality() {
        var eventId = EventId.generate();
        var operationId = ProjectHistoryReplacementOperationId.generate();
        var projectId = ProjectId.generate();
        var blobLocation = new BlobLocation("some-bucket", "some-name");

        var event1 = new ProjectHistoryReplacementSucceededEvent(eventId, operationId, projectId, blobLocation);
        var event2 = new ProjectHistoryReplacementSucceededEvent(eventId, operationId, projectId, blobLocation);

        assertThat(event1).isEqualTo(event2);
        assertThat(event1.hashCode()).isEqualTo(event2.hashCode());
    }

    @Test
    void shouldSerializeAndDeserialize() throws JsonProcessingException {
        var eventId = EventId.generate();
        var operationId = ProjectHistoryReplacementOperationId.generate();
        var projectId = ProjectId.generate();
        var blobLocation = new BlobLocation("some-bucket", "some-name");

        var event = new ProjectHistoryReplacementSucceededEvent(eventId, operationId, projectId, blobLocation);

        var json = objectMapper.writeValueAsString(event);

        assertThat(json)
                .contains("\"eventId\"")
                .contains("\"operationId\"")
                .contains("\"projectId\"")
                .contains("\"blobLocation\"");

        var roundTripped = objectMapper.readValue(json, ProjectHistoryReplacementSucceededEvent.class);

        assertThat(roundTripped).isEqualTo(event);
    }
}
