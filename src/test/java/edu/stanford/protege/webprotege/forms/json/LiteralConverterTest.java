package edu.stanford.protege.webprotege.forms.json;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.vocab.OWL2Datatype;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LiteralConverterTest {

    private LiteralConverter converter;

    private JsonNodeFactory nodeFactory;

    @Mock
    private OWLLiteral literal;

    @Mock
    private OWLDatatype datatype;

    @BeforeEach
    void setUp() {
        converter = new LiteralConverter();
        nodeFactory = JsonNodeFactory.instance;
    }

    @Test
    void shouldConvertIntegerLiteral() {
        var intValue = "42";
        when(literal.getLiteral()).thenReturn(intValue);
        when(literal.getDatatype()).thenReturn(datatype);
        when(datatype.getIRI()).thenReturn(OWL2Datatype.XSD_INT.getIRI());
        var result = converter.convert(literal);
        assertThat(result).isEqualTo(nodeFactory.numberNode(42));
    }

    @Test
    void shouldConvertUnsignedIntLiteral() {
        var intValue = "42";
        when(literal.getLiteral()).thenReturn(intValue);
        when(literal.getDatatype()).thenReturn(datatype);
        when(datatype.getIRI()).thenReturn(OWL2Datatype.XSD_UNSIGNED_INT.getIRI());
        var result = converter.convert(literal);
        assertThat(result).isEqualTo(nodeFactory.numberNode(42));
    }

    @Test
    void shouldConvertLongLiteral() {
        var longValue = "1234567890";
        when(literal.getLiteral()).thenReturn(longValue);
        when(literal.getDatatype()).thenReturn(datatype);
        when(datatype.getIRI()).thenReturn(OWL2Datatype.XSD_LONG.getIRI());
        var result = converter.convert(literal);
        assertThat(result).isEqualTo(nodeFactory.numberNode(1234567890L));
    }

    @Test
    void shouldConvertDoubleLiteral() {
        var doubleValue = "3.14";
        when(literal.getLiteral()).thenReturn(doubleValue);
        when(literal.getDatatype()).thenReturn(datatype);
        when(datatype.getIRI()).thenReturn(OWL2Datatype.XSD_DOUBLE.getIRI());
        var result = converter.convert(literal);
        assertThat(result).isEqualTo(nodeFactory.numberNode(3.14));
    }

    @Test
    void shouldConvertFloatLiteral() {
        var floatValue = "3.14";
        when(literal.getLiteral()).thenReturn(floatValue);
        when(literal.getDatatype()).thenReturn(datatype);
        when(datatype.getIRI()).thenReturn(OWL2Datatype.XSD_FLOAT.getIRI());
        var result = converter.convert(literal);
        assertThat(result).isEqualTo(nodeFactory.numberNode(3.14f));
    }

    @Test
    void shouldConvertToTextNodeForUnsupportedDatatype() {
        var unsupportedValue = "unsupported";
        when(literal.getLiteral()).thenReturn(unsupportedValue);
        when(literal.getDatatype()).thenReturn(datatype);
        when(datatype.getIRI()).thenReturn(OWL2Datatype.XSD_STRING.getIRI()); // Note not explicitly handled
        var result = converter.convert(literal);
        assertThat(result).isEqualTo(nodeFactory.textNode(unsupportedValue));
    }

    @Test
    void shouldHandleNumberFormatExceptionGracefully() {
        var invalidNumber = "not_a_number";
        when(literal.getLiteral()).thenReturn(invalidNumber);
        when(literal.getDatatype()).thenReturn(datatype);
        when(datatype.getIRI()).thenReturn(OWL2Datatype.XSD_INT.getIRI()); // Expected to be an integer
        var result = converter.convert(literal);
        assertThat(result).isEqualTo(nodeFactory.textNode(invalidNumber));
    }
}
