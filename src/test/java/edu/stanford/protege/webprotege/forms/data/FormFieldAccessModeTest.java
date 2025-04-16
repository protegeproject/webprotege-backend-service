package edu.stanford.protege.webprotege.forms.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.boot.test.json.ObjectContent;

import static org.assertj.core.api.Assertions.assertThat;

class FormFieldAccessMode_Serialization_Test {

    private JacksonTester<FormFieldAccessMode> tester;

    @BeforeEach
    void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    void shouldSerializeReadOnly() throws Exception {
        JsonContent<FormFieldAccessMode> json = tester.write(FormFieldAccessMode.READ_ONLY);
        assertThat(json).isEqualToJson("\"ReadOnly\"");
    }

    @Test
    void shouldSerializeReadWrite() throws Exception {
        JsonContent<FormFieldAccessMode> json = tester.write(FormFieldAccessMode.READ_WRITE);
        assertThat(json).isEqualToJson("\"ReadWrite\"");
    }

    @Test
    void shouldDeserializeReadOnly() throws Exception {
        ObjectContent<FormFieldAccessMode> object = tester.parse("\"ReadOnly\"");
        assertThat(object.getObject()).isEqualTo(FormFieldAccessMode.READ_ONLY);
    }

    @Test
    void shouldDeserializeReadWrite() throws Exception {
        ObjectContent<FormFieldAccessMode> object = tester.parse("\"ReadWrite\"");
        assertThat(object.getObject()).isEqualTo(FormFieldAccessMode.READ_WRITE);
    }
}