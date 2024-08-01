package edu.stanford.protege.webprotege.forms.data;

import com.google.common.collect.ImmutableMap;
import edu.stanford.protege.webprotege.entity.*;
import edu.stanford.protege.webprotege.jackson.WebProtegeJacksonApplication;
import org.junit.jupiter.api.Test;
import org.semanticweb.owlapi.model.IRI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.*;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.context.annotation.Import;
import uk.ac.manchester.cs.owl.owlapi.*;

import java.io.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2024-07-03
 */
@JsonTest
@AutoConfigureJsonTesters
@Import(WebProtegeJacksonApplication.class)
public class PrimitiveFormControlDataDtoTest {

    @Autowired
    private JacksonTester<PrimitiveFormControlDataDto> tester;

    @Test
    public void shouldSerializeEntityData() throws IOException {
        var data = PrimitiveFormControlDataDto.get(OWLClassData.get(new OWLClassImpl(IRI.create("http://example.org/A")), ImmutableMap.of()));
        var written = tester.write(data);
        System.out.println(written.getJson());
        assertThat(written).hasJsonPathStringValue("['@type']", "EntityFormControlDataDto");
        assertThat(written).hasJsonPathValue("entity");
    }

    @Test
    void shouldDeserializeEntityData() throws IOException {
        var expected = PrimitiveFormControlDataDto.get(OWLClassData.get(new OWLClassImpl(IRI.create("http://example.org/A")), ImmutableMap.of()));
        var json = """
                {"@type":"EntityFormControlDataDto","entity":{"@type":"ClassData","iri":"http://example.org/A"}}
                """;
        var read = tester.read(new StringReader(json));
        assertThat(read.getObject()).isEqualTo(expected);
    }

    @Test
    public void shouldSerializeIriData() throws IOException {
        var data = PrimitiveFormControlDataDto.get(IRIData.get(IRI.create("http://example.org/A"), ImmutableMap.of()));
        var written = tester.write(data);
        System.out.println(written.getJson());
        assertThat(written).hasJsonPathStringValue("['@type']", "IriFormControlDataDto");
        assertThat(written).hasJsonPathValue("iri");
    }

    @Test
    void shouldDeserializeIriData() throws IOException {
        var expected = PrimitiveFormControlDataDto.get(IRIData.get(IRI.create("http://example.org/A"), ImmutableMap.of()));
        var json = """
                {"@type":"IriFormControlDataDto","iri":{"@type":"IRIData","iri":"http://example.org/A"}}
                """;
        var read = tester.read(new StringReader(json));
        assertThat(read.getObject()).isEqualTo(expected);
    }

    @Test
    public void shouldSerializeLiteralData() throws IOException {
        var data = PrimitiveFormControlDataDto.get(OWLLiteralData.get(new OWLLiteralImpl("abc", "en", null)));
        var written = tester.write(data);
        System.out.println(written.getJson());
        assertThat(written).hasJsonPathStringValue("['@type']", "LiteralFormControlDataDto");
        assertThat(written).hasJsonPathValue("literal");
    }

    @Test
    void shouldDeserializeLiteralData() throws IOException {
        var expected = PrimitiveFormControlDataDto.get(OWLLiteralData.get(new OWLLiteralImpl("abc", "en", null)));
        var json = """
                {"@type":"LiteralFormControlDataDto","literal":{"@type":"LiteralData","value":"abc","lang":"en"}}
                """;
        var read = tester.read(new StringReader(json));
        assertThat(read.getObject()).isEqualTo(expected);
    }
}
