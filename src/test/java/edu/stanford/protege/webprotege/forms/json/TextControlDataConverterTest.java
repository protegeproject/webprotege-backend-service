package edu.stanford.protege.webprotege.forms.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import edu.stanford.protege.webprotege.forms.data.TextControlData;
import edu.stanford.protege.webprotege.forms.json.TextControlDataConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.semanticweb.owlapi.model.OWLLiteral;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TextControlDataConverterTest {

    private TextControlDataConverter converter;

    private JsonNodeFactory nodeFactory;

    @Mock
    private TextControlData textControlData;

    @Mock
    private OWLLiteral owlLiteral;

    @BeforeEach
    void setUp() {
        converter = new TextControlDataConverter();
        nodeFactory = JsonNodeFactory.instance;
    }

    @Test
    void shouldConvertTextControlDataWithLiteralValue() {
        when(owlLiteral.getLiteral()).thenReturn("Sample Text");
        when(textControlData.getValue()).thenReturn(Optional.of(owlLiteral));
        var result = converter.convert(textControlData);
        assertThat(result).isEqualTo(nodeFactory.textNode("Sample Text"));
    }

    @Test
    void shouldConvertTextControlDataWithEmptyValue() {
        when(textControlData.getValue()).thenReturn(Optional.empty());
        var result = converter.convert(textControlData);
        assertThat(result).isEqualTo(nodeFactory.textNode(""));
    }
}