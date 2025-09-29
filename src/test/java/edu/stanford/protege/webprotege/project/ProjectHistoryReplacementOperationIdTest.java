package edu.stanford.protege.webprotege.project;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class ProjectHistoryReplacementOperationIdTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldCreateFromValueAndExposeId() {
        var raw = "op-123";
        var id = ProjectHistoryReplacementOperationId.valueOf(raw);
        assertThat(id.id()).isEqualTo(raw);
    }

    @Test
    void shouldThrowNpeForNullIdInConstructor() {
        assertThatNullPointerException()
                .isThrownBy(() -> new ProjectHistoryReplacementOperationId(null));
    }

    @Test
    void shouldThrowNpeForNullIdInValueOf() {
        assertThatNullPointerException()
                .isThrownBy(() -> ProjectHistoryReplacementOperationId.valueOf(null));
    }

    @Test
    void shouldGenerateRandomUuidString() {
        var id1 = ProjectHistoryReplacementOperationId.generate();
        var id2 = ProjectHistoryReplacementOperationId.generate();

        assertThat(id1).isNotEqualTo(id2);

        assertThatCode(() -> UUID.fromString(id1.id())).doesNotThrowAnyException();
        assertThatCode(() -> UUID.fromString(id2.id())).doesNotThrowAnyException();
    }

    @Test
    void shouldImplementEqualityAndHashCode() {
        var raw = "same-id";
        var a = ProjectHistoryReplacementOperationId.valueOf(raw);
        var b = ProjectHistoryReplacementOperationId.valueOf(raw);

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
        assertThat(a).hasToString(b.toString());
    }

    @Test
    void shouldSerializeAsJsonString() throws JsonProcessingException {
        var id = ProjectHistoryReplacementOperationId.valueOf("abc-123");
        var json = objectMapper.writeValueAsString(id);
        assertThat(json).isEqualTo("\"abc-123\"");
    }

    @Test
    void shouldDeserializeFromJsonString() throws JsonProcessingException {
        var json = "\"xyz-789\"";
        var id = objectMapper.readValue(json, ProjectHistoryReplacementOperationId.class);
        assertThat(id.id()).isEqualTo("xyz-789");
    }

    @Test
    void shouldRoundTripJson() throws JsonProcessingException {
        var original = ProjectHistoryReplacementOperationId.generate();
        var json = objectMapper.writeValueAsString(original);
        var roundTripped = objectMapper.readValue(json, ProjectHistoryReplacementOperationId.class);
        assertThat(roundTripped).isEqualTo(original);
    }
}
