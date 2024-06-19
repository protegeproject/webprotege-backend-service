package edu.stanford.protege.webprotege.forms;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.protege.webprotege.forms.data.PrimitiveFormControlData;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.*;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2024-06-18
 */
@JsonTest
@AutoConfigureJsonTesters
class PrimitiveFormControlDataTest {

    @Autowired
    private JacksonTester<PrimitiveFormControlData> tester;

    @BeforeEach
    void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    void shouldSerializeBooleanLiteral() throws IOException {
        var data = PrimitiveFormControlData.get(true);
        var written = tester.write(data);
        assertThat(written).hasJsonPathStringValue("value", "true");
        assertThat(written).hasJsonPathStringValue("datatype", "http://www.w3.org/2001/XMLSchema#boolean");
        System.out.println(written.getJson());
    }
}
