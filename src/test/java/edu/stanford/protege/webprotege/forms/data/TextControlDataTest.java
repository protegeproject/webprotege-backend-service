package edu.stanford.protege.webprotege.forms.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.protege.webprotege.forms.field.TextControlDescriptor;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class TextControlDataTest {

    private JacksonTester<TextControlData> tester;

    @BeforeEach
    void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    void shouldSerialize() throws IOException {
        var data = TextControlData.get(TextControlDescriptor.getDefault(),
                            null
        );
        var written = tester.write(data);
        System.out.println(written.getJson());
        assertThat(written).hasJsonPath("value");
        assertThat(written).hasJsonPathMapValue("control");
    }
}