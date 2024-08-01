package edu.stanford.protege.webprotege.forms.field;



import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.json.JacksonTester;

import java.io.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FormRegionId_TestCase {

    public static final String UUID = "12345678-1234-1234-1234-123456789abc";

    private JacksonTester<FormRegionId> tester;

    @BeforeEach
    void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    public void shouldGetFormFieldIdWithSuppliedUUID() {
        FormRegionId id = FormRegionId.get(UUID);
        assertThat(id.value()).isEqualTo(UUID);
    }

    @Test
    public void shouldNotAcceptMalformedId() {
        assertThrows(IllegalArgumentException.class, () -> {
            FormRegionId.get("NotAUUID");
        });
    }

    @Test
    public void shouldNotAcceptNull() {
        assertThrows(NullPointerException.class, () -> {
            FormRegionId.get(null);
        });
    }

    @Test
    void shouldSerialize() throws IOException {
        var formRegionId = FormRegionId.generate();
        var written = tester.write(formRegionId);
        assertThat(written.getJson()).isEqualTo("\"" + formRegionId.value() + "\"");
    }

    @Test
    void shouldDeserialize() throws IOException {
        var read = tester.read(new StringReader("""
                            "12345678-1234-1234-1234-123456789abc"
                            """));
        assertThat(read.getObject().value()).isEqualTo("12345678-1234-1234-1234-123456789abc");
    }
}