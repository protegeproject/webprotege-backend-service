package edu.stanford.protege.webprotege.forms.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.*;
import org.springframework.boot.test.json.JacksonTester;

import java.io.*;

import static org.assertj.core.api.Assertions.assertThat;

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
        System.out.println(written.getJson());
        assertThat(written).hasJsonPathStringValue("value", "true");
        assertThat(written).hasJsonPathValue("type");
    }

    @Test
    void shouldDeserializeBooleanLiteral() throws IOException {
        var json = """
                {"value":"true","type":"http://www.w3.org/2001/XMLSchema#boolean"}
                """;
        var read =tester.read(new StringReader(json));
        assertThat(read.getObject().asLiteral()).isPresent();
        assertThat(read.getObject().asLiteral().get().getLiteral()).isEqualTo("true");
        assertThat(read.getObject().asLiteral().get().isBoolean()).isTrue();
    }
}