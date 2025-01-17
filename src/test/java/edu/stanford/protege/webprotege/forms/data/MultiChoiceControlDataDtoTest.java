package edu.stanford.protege.webprotege.forms.data;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import edu.stanford.protege.webprotege.entity.IRIData;
import edu.stanford.protege.webprotege.forms.field.FixedChoiceListSourceDescriptor;
import edu.stanford.protege.webprotege.forms.field.MultiChoiceControlDescriptor;
import edu.stanford.protege.webprotege.jackson.WebProtegeJacksonApplication;
import org.junit.jupiter.api.Test;
import org.semanticweb.owlapi.model.IRI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.context.annotation.Import;

import java.io.IOException;
import java.io.StringReader;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@AutoConfigureJsonTesters
@Import(WebProtegeJacksonApplication.class)
class MultiChoiceControlDataDtoTest {

    @Autowired
    private JacksonTester<MultiChoiceControlDataDto> tester;

    @Test
    void shouldSerialize() throws IOException {
        var data = MultiChoiceControlDataDto.get(MultiChoiceControlDescriptor.get(FixedChoiceListSourceDescriptor.get(
                                                         ImmutableList.of()), ImmutableList.of()),
                                                 ImmutableList.of(PrimitiveFormControlDataDto.get(IRIData.get(IRI.create(
                                                         "http://example.org/A"), ImmutableMap.of()))),
                                                 3);
        var written = tester.write(data);
        System.out.println(written.getJson());
        assertThat(written).hasJsonPathStringValue("['@type']", "MultiChoiceControlDataDto");
        assertThat(written).hasJsonPathValue("control");
        assertThat(written).hasJsonPathValue("values");
        assertThat(written).hasJsonPathValue("depth");

    }

    @Test
    void shouldDeserialize() throws IOException {
        var expected = MultiChoiceControlDataDto.get(MultiChoiceControlDescriptor.get(FixedChoiceListSourceDescriptor.get(
                                                         ImmutableList.of()), ImmutableList.of()),
                                                 ImmutableList.of(PrimitiveFormControlDataDto.get(IRIData.get(IRI.create(
                                                         "http://example.org/A"), ImmutableMap.of()))),
                                                 3);
        var json = """
                {"@type":"MultiChoiceControlDataDto","depth":3,"control":{"@type":"MULTI_CHOICE","choicesSource":{"@type":"Fixed","choices":[]},"defaultChoice":[]},"values":[{"@type":"IriFormControlDataDto","iri":{"@type":"IRIData","iri":"http://example.org/A"}}]}
                """;
        var read = tester.read(new StringReader(json));
        assertThat(read.getObject()).isEqualTo(expected);
    }
}