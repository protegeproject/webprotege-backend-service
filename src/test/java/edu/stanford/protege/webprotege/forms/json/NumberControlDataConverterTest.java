package edu.stanford.protege.webprotege.forms.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import edu.stanford.protege.webprotege.forms.data.NumberControlData;
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
class NumberControlDataConverterTest {

    private NumberControlDataConverter converter;

    private JsonNodeFactory nodeFactory;

    @Mock
    private NumberControlData numberControlData;

    @Mock
    private LiteralConverter literalConverter;

    @Mock
    private OWLLiteral owlLiteral;

    @Mock
    private JsonNode mockJsonNode;

    @BeforeEach
    void setUp() {
        converter = new NumberControlDataConverter(literalConverter);
        nodeFactory = JsonNodeFactory.instance;
    }

    @Test
    void shouldConvertNumberControlDataWithLiteralValue() {
        when(numberControlData.getValue()).thenReturn(Optional.of(owlLiteral));
        when(literalConverter.convert(owlLiteral)).thenReturn(mockJsonNode);
        var result = converter.convert(numberControlData);
        assertThat(result).isEqualTo(mockJsonNode);
    }

    @Test
    void shouldConvertNumberControlDataWithEmptyValue() {
        when(numberControlData.getValue()).thenReturn(Optional.empty());
        var result = converter.convert(numberControlData);
        assertThat(result).isEqualTo(nodeFactory.nullNode());
    }
}