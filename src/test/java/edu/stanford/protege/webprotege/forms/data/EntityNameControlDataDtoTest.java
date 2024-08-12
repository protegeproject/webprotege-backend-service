package edu.stanford.protege.webprotege.forms.data;

import edu.stanford.protege.webprotege.forms.field.*;
import edu.stanford.protege.webprotege.jackson.WebProtegeJacksonApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.context.annotation.Import;
import uk.ac.manchester.cs.owl.owlapi.OWLLiteralImpl;

import java.io.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JsonTest
@Import(WebProtegeJacksonApplication.class)
class EntityNameControlDataDtoTest {

    @Autowired
    private JacksonTester<FormControlDataDto> tester;

    @Test
    void shouldSerialize() throws IOException {
        var data = EntityNameControlDataDto.get(EntityNameControlDescriptor.getDefault().getDefault(),
                                                null,
                                                3);
        var written = tester.write(data);
        System.out.println(written.getJson());
        assertThat(written).hasJsonPathValue("control");
        assertThat(written).hasJsonPath("value");
        assertThat(written).hasJsonPathValue("depth");
    }

    @Test
    void shouldDeserialize() throws IOException {
        var json = """
                {
                	"@type": "EntityNameControlDataDto",
                	"depth": 3,
                	"control": {
                		"@type": "ENTITY_NAME"
                	},
                	"value": null
                }
                """;
        var read = tester.read(new StringReader(json));
        assertThat(read.getObject()).isInstanceOf(EntityNameControlDataDto.class);
        var obj = (EntityNameControlDataDto) read.getObject();
        assertThat(obj.getEntity()).isEmpty();
        assertThat(obj.getDepth()).isEqualTo(3);
    }
}