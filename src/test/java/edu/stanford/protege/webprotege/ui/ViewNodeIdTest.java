package edu.stanford.protege.webprotege.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

class ViewNodeIdTest {

    private JacksonTester<ViewNodeId> tester;

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    public void shouldThrowNpe() {
        assertThrows(NullPointerException.class, () -> {
            new ViewNodeId(null);
        });
    }

    @Test
    public void shouldDeserializeFromJsonString() throws IOException {
        var json = """
                    "32f71e70-ca29-4b40-91a7-966684f1897f"
                """;
        var parsed = tester.parse(json);
        assertThat(parsed.getObject().value()).isEqualTo("32f71e70-ca29-4b40-91a7-966684f1897f");
    }

    @Test
    public void shouldSerializeAsJsonString() throws IOException {
        var written = tester.write(ViewNodeId.valueOf("75ff7d52-6a5e-4314-a53d-358271de48c4"));
        assertThat(written.getJson()).isEqualTo("""
                "75ff7d52-6a5e-4314-a53d-358271de48c4"
                """.trim());
    }

}