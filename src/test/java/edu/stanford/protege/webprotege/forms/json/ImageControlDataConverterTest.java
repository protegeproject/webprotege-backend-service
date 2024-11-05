package edu.stanford.protege.webprotege.forms.json;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import edu.stanford.protege.webprotege.forms.data.ImageControlData;
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
class ImageControlDataConverterTest {

    private ImageControlDataConverter converter;

    private JsonNodeFactory nodeFactory;

    @Mock
    private ImageControlData imageControlData;

    @BeforeEach
    void setUp() {
        converter = new ImageControlDataConverter();
        nodeFactory = JsonNodeFactory.instance;
    }

    @Test
    void shouldConvertImageControlDataWithIri() {
        var iriString = "http://example.com/image.jpg";
        when(imageControlData.getIri()).thenReturn(Optional.of(IRI.create(iriString)));
        var result = converter.convert(imageControlData);
        assertThat(result).isEqualTo(nodeFactory.textNode(iriString));
    }

    @Test
    void shouldConvertImageControlDataWithIriWithWhitespace() {
        var iriString = "  http://example.com/image.jpg  ";
        when(imageControlData.getIri()).thenReturn(Optional.of(IRI.create(iriString)));
        var result = converter.convert(imageControlData);
        assertThat(result).isEqualTo(nodeFactory.textNode(iriString.trim()));
    }

    @Test
    void shouldConvertImageControlDataWithNoIri() {
        when(imageControlData.getIri()).thenReturn(Optional.empty());
        var result = converter.convert(imageControlData);
        assertThat(result).isEqualTo(nodeFactory.nullNode());
    }
}
