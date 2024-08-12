package edu.stanford.protege.webprotege.forms.data;

import com.google.common.collect.*;
import edu.stanford.protege.webprotege.entity.IRIData;
import edu.stanford.protege.webprotege.forms.field.*;
import edu.stanford.protege.webprotege.jackson.WebProtegeJacksonApplication;
import org.junit.jupiter.api.Test;
import org.semanticweb.owlapi.model.IRI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.*;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.context.annotation.Import;

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
public class SingleChoiceControlDataDtoTest {

    @Autowired
    private JacksonTester<SingleChoiceControlDataDto> tester;

    @Test
    void shouldSerialize() throws IOException {
        var data = SingleChoiceControlDataDto.get(SingleChoiceControlDescriptor.get(
                SingleChoiceControlType.COMBO_BOX,
                FixedChoiceListSourceDescriptor.get(ImmutableList.of())),
                                                 PrimitiveFormControlDataDto.get(IRIData.get(IRI.create(
                                                         "http://example.org/A"), ImmutableMap.of())),
                                                 3);
        var written = tester.write(data);
        System.out.println(written.getJson());
        assertThat(written).hasJsonPathStringValue("['@type']", "SingleChoiceControlDataDto");
        assertThat(written).hasJsonPathValue("control");
        assertThat(written).hasJsonPathValue("value");
        assertThat(written).hasJsonPathValue("depth");

    }

    @Test
    void shouldDeserialize() throws IOException {
        var expected = SingleChoiceControlDataDto.get(SingleChoiceControlDescriptor.get(
                                                          SingleChoiceControlType.COMBO_BOX,
                                                          FixedChoiceListSourceDescriptor.get(ImmutableList.of())),
                                                  PrimitiveFormControlDataDto.get(IRIData.get(IRI.create(
                                                          "http://example.org/A"), ImmutableMap.of())),
                                                  3);
        var json = """
                {"@type":"SingleChoiceControlDataDto","depth":3,"control":{"@type":"SINGLE_CHOICE","widgetType":"ComboBox","choicesSource":{"@type":"Fixed","choices":[]},"defaultChoice":null},"value":{"@type":"IriFormControlDataDto","iri":{"@type":"IRIData","iri":"http://example.org/A"}}}
                """;
        var read = tester.read(new StringReader(json));
        assertThat(read.getObject()).isEqualTo(expected);
    }
}
