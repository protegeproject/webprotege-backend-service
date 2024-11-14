package edu.stanford.protege.webprotege.forms.json;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.stanford.protege.webprotege.MockingUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.semanticweb.owlapi.model.IRI;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IriConverterTest {

    private IriConverter converter;

    private JsonNodeFactory nodeFactory;

    @Mock
    private JsonNameExtractor nameExtractor;

    private final IRI iri = MockingUtils.mockIRI();

    @BeforeEach
    void setUp() {
        converter = new IriConverter(nameExtractor);
        nodeFactory = JsonNodeFactory.instance;
    }

    @Test
    void shouldConvertIriWithJsonNameOverride() {
        var overrideName = "Custom Name";
        when(nameExtractor.getJsonName(iri)).thenReturn(Optional.of(overrideName));
        var result = converter.convert(iri);
        assertThat(result).isEqualTo(nodeFactory.textNode(overrideName));
    }

    @Test
    void shouldConvertIriWithoutJsonNameOverride() {
        when(nameExtractor.getJsonName(iri)).thenReturn(Optional.empty());
        var result = converter.convert(iri);
        assertThat(result).isInstanceOf(ObjectNode.class);
        assertThat(result.get(IriConverter.IRI_KEY).asText()).isEqualTo(iri.toString());
    }
}
