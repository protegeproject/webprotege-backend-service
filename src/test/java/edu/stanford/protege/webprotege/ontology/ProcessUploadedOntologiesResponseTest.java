package edu.stanford.protege.webprotege.ontology;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.protege.webprotege.common.BlobLocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.List;

public class ProcessUploadedOntologiesResponseTest {

    private JacksonTester<ProcessUploadedOntologiesResponse> tester;

    @BeforeEach
    public void setup() {
        var objectMapper = new ObjectMapper();
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    void serializesToJson() throws Exception {
        var location = new BlobLocation("example-bucket", "ontology123.bin");
        var response = new ProcessUploadedOntologiesResponse(List.of(location));

        var jsonContent = tester.write(response);

        assertThat(jsonContent).extractingJsonPathStringValue("$.ontologies[0].bucket").isEqualTo("example-bucket");
        assertThat(jsonContent).extractingJsonPathStringValue("$.ontologies[0].name").isEqualTo("ontology123.bin");
    }

    @Test
    void deserializesFromJson() throws Exception {
        var jsonInput = """
                {
                  "ontologies": [
                    {
                      "bucket": "example-bucket",
                      "name": "ontology123.json"
                    }
                  ]
                }
                """;
        var parsedResponse = tester.parse(jsonInput).getObject();

        assertThat(parsedResponse.ontologies()).hasSize(1);
        assertThat(parsedResponse.ontologies().get(0).bucket()).isEqualTo("example-bucket");
        assertThat(parsedResponse.ontologies().get(0).name()).isEqualTo("ontology123.json");
    }

    @Test
    void constructorThrowsNullPointerExceptionIfOntologiesIsNull() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new ProcessUploadedOntologiesResponse(null));
    }
}
