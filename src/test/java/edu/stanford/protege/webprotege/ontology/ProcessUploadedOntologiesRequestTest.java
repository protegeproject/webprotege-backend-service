package edu.stanford.protege.webprotege.ontology;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.protege.webprotege.csv.DocumentId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class ProcessUploadedOntologiesRequestTest {

    private JacksonTester<ProcessUploadedOntologiesRequest> json;

    @BeforeEach
    public void setup() {
        ObjectMapper objectMapper = new ObjectMapper();
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    void serializesToJson() throws Exception {
        var documentId = new DocumentId("123");
        var request = new ProcessUploadedOntologiesRequest(documentId);
        JsonContent<ProcessUploadedOntologiesRequest> jsonContent = json.write(request);
        assertThat(jsonContent).hasJsonPathStringValue("@.fileSubmissionId", "123");
    }

    @Test
    void deserializesFromJson() throws Exception {
        String content = """
                        {"fileSubmissionId":"123"}""
                """;
        assertThat(json.parse(content))
                .usingRecursiveComparison()
                .isEqualTo(new ProcessUploadedOntologiesRequest(new DocumentId("123")));
    }

    @Test
    void throwsNullPointerExceptionIfDocumentIdIsNull() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new ProcessUploadedOntologiesRequest(null));
    }
}
