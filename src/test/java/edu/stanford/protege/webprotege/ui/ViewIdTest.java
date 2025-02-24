package edu.stanford.protege.webprotege.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

class ViewIdTest {

    private JacksonTester<ViewId> tester;

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    public void shouldThrowNpe() {
        assertThrows(NullPointerException.class, () -> {
           new ViewId(null);
        });
    }

    @Test
    public void shouldDeserializeFromJsonString() throws IOException {
        var json = """
                    "c8c6ad05-54cb-4369-a017-595340070b88"
                """;
        var parsed = tester.parse(json);
        assertThat(parsed.getObject().value()).isEqualTo("c8c6ad05-54cb-4369-a017-595340070b88");
    }

    @Test
    public void shouldSerializeAsJsonString() throws IOException {
        var written = tester.write(ViewId.valueOf("b339ff95-fcc2-430d-bccd-51a54057409e"));
        assertThat(written.getJson()).isEqualTo("""
                "b339ff95-fcc2-430d-bccd-51a54057409e"
                """.trim());
    }

}