package edu.stanford.protege.webprotege.revision;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.protege.webprotege.common.BlobLocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.json.JacksonTester;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class CreateInitialRevisionHistoryResponseTest {

    private JacksonTester<CreateInitialRevisionHistoryResponse> tester;

    @BeforeEach
    public void setup() {
        var objectMapper = new ObjectMapper();
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    void serializesToJson() throws Exception {
        var location = new BlobLocation("my-bucket", "revision-history");
        var response = new CreateInitialRevisionHistoryResponse(location);

        var jsonContent = tester.write(response);
        assertThat(jsonContent).hasJsonPathStringValue("$.documentLocation.bucket", "my-bucket");
        assertThat(jsonContent).hasJsonPathStringValue("$.documentLocation.name", "revision-history");
    }

    @Test
    void deserializesFromJson() throws Exception {
        var jsonInput = """
                {
                  "documentLocation": {
                    "bucket": "my-bucket",
                    "name": "revision-history"
                  }
                }
                """;
        var parsedResponse = tester.parse(jsonInput).getObject();

        assertThat(parsedResponse.revisionHistoryLocation()).isNotNull();
        assertThat(parsedResponse.revisionHistoryLocation().bucket()).isEqualTo("my-bucket");
        assertThat(parsedResponse.revisionHistoryLocation().name()).isEqualTo("revision-history");
    }

    @Test
    void throwsNullPointerExceptionIfRevisionHistoryLocationIsNull() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new CreateInitialRevisionHistoryResponse(null));
    }
}
