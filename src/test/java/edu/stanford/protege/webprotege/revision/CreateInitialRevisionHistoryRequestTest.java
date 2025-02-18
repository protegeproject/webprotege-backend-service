package edu.stanford.protege.webprotege.revision;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.protege.webprotege.common.BlobLocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.json.JacksonTester;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class CreateInitialRevisionHistoryRequestTest {

    private JacksonTester<CreateInitialRevisionHistoryRequest> json;

    @BeforeEach
    public void setup() {
        var objectMapper = new ObjectMapper();
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    void serializesToJson() throws Exception {
        var location = new BlobLocation("my-bucket", "revision1");
        var request = new CreateInitialRevisionHistoryRequest(List.of(location));

        var jsonContent = json.write(request);

        assertThat(jsonContent).hasJsonPathStringValue("$.documentLocations[0].bucket", "my-bucket");
        assertThat(jsonContent).hasJsonPathStringValue("$.documentLocations[0].name", "revision1");
    }

    @Test
    void deserializesFromJson() throws Exception {
        var jsonInput = """
                {
                  "documentLocations": [
                    {
                      "bucket": "my-bucket",
                      "name": "revision1"
                    }
                  ]
                }
                """;
        var parsedRequest = json.parse(jsonInput).getObject();

        assertThat(parsedRequest.documentLocations()).hasSize(1);
        assertThat(parsedRequest.documentLocations().get(0).bucket()).isEqualTo("my-bucket");
        assertThat(parsedRequest.documentLocations().get(0).name()).isEqualTo("revision1");
    }

    @Test
    void throwsNullPointerExceptionIfDocumentLocationsIsNull() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new CreateInitialRevisionHistoryRequest(null));
    }
}
