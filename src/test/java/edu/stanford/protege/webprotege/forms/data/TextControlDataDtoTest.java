package edu.stanford.protege.webprotege.forms.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.protege.webprotege.forms.field.TextControlDescriptor;
import edu.stanford.protege.webprotege.jackson.WebProtegeJacksonApplication;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.context.annotation.Import;
import uk.ac.manchester.cs.owl.owlapi.OWLLiteralImpl;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JsonTest
@Import(WebProtegeJacksonApplication.class)
class TextControlDataDtoTest {

    @Autowired
    private JacksonTester<FormControlDataDto> tester;

    @Test
    void shouldSerialize() throws IOException {
        var data = TextControlDataDto.get(TextControlDescriptor.getDefault(),
                                          new OWLLiteralImpl("Hello", "en", null),
                                          3);
        var written = tester.write(data);
        System.out.println(written.getJson());
        assertThat(written).hasJsonPathValue("control");
        assertThat(written).hasJsonPathValue("value");
        assertThat(written).hasJsonPathValue("depth");
    }
}