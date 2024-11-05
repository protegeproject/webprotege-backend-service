package edu.stanford.protege.webprotege.forms.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import edu.stanford.protege.webprotege.MockingUtils;
import edu.stanford.protege.webprotege.forms.data.PrimitiveFormControlData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLLiteral;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PrimitiveFormControlDataConverterTest {

    private PrimitiveFormControlDataConverter converter;

    private JsonNodeFactory nodeFactory;

    @Mock
    private PrimitiveFormControlData data;

    @Mock
    private LiteralConverter literalConverter;

    @Mock
    private IriConverter iriConverter;

    @Mock
    private EntityConverter entityConverter;

    @Mock
    private OWLLiteral owlLiteral;

    private IRI iri = MockingUtils.mockIRI();

    @Mock
    private OWLEntity entity;

    @Mock
    private JsonNode mockLiteralJsonNode;

    @Mock
    private JsonNode mockIriJsonNode;

    @Mock
    private JsonNode mockEntityJsonNode;

    @BeforeEach
    void setUp() {
        converter = new PrimitiveFormControlDataConverter(literalConverter, iriConverter, entityConverter);
        nodeFactory = JsonNodeFactory.instance;
    }

    @Test
    void shouldConvertLiteralData() {
        when(data.asLiteral()).thenReturn(Optional.of(owlLiteral));
        when(literalConverter.convert(owlLiteral)).thenReturn(mockLiteralJsonNode);
        var result = converter.convert(data);
        assertThat(result).isEqualTo(mockLiteralJsonNode);
    }

    @Test
    void shouldConvertIriData() {
        when(data.asLiteral()).thenReturn(Optional.empty());
        when(data.asIri()).thenReturn(Optional.of(iri));
        when(iriConverter.convert(iri)).thenReturn(mockIriJsonNode);
        var result = converter.convert(data);
        assertThat(result).isEqualTo(mockIriJsonNode);
    }

    @Test
    void shouldConvertEntityData() {
        when(data.asLiteral()).thenReturn(Optional.empty());
        when(data.asIri()).thenReturn(Optional.empty());
        when(data.asEntity()).thenReturn(Optional.of(entity));
        when(entityConverter.convert(entity)).thenReturn(mockEntityJsonNode);

        var result = converter.convert(data);

        assertThat(result).isEqualTo(mockEntityJsonNode);
    }

    @Test
    void shouldReturnNullNodeWhenNoDataPresent() {
        when(data.asLiteral()).thenReturn(Optional.empty());
        when(data.asIri()).thenReturn(Optional.empty());
        when(data.asEntity()).thenReturn(Optional.empty());

        var result = converter.convert(data);

        assertThat(result).isEqualTo(nodeFactory.nullNode());
    }
}
