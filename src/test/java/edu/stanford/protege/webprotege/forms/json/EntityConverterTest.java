package edu.stanford.protege.webprotege.forms.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.stanford.protege.webprotege.MockingUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.EntityType;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EntityConverterTest {

    private EntityConverter converter;

    private JsonNodeFactory nodeFactory;

    @Mock
    private JsonNameExtractor nameExtractor;

    private OWLEntity entity;

    private IRI entityIri;

    private EntityType<?> entityType;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        converter = new EntityConverter(nameExtractor);
        nodeFactory = JsonNodeFactory.instance;
        entity = MockingUtils.mockOWLClass();
        entityIri = entity.getIRI();
        entityType = EntityType.CLASS;
    }

    @Test
    void shouldConvertEntityWithJsonNameOverride() {
        var overrideName = "Custom Name";
        when(nameExtractor.getJsonName(entityIri)).thenReturn(Optional.of(overrideName));
        var result = converter.convert(entity);
        assertThat(result).isEqualTo(nodeFactory.textNode(overrideName));
    }

    @Test
    void shouldConvertEntityWithoutJsonNameOverride() {
        when(nameExtractor.getJsonName(entityIri)).thenReturn(Optional.empty());
        var result = converter.convert(entity);
        assertThat(result).isInstanceOf(ObjectNode.class);
        assertThat(result.get(EntityConverter.IRI_KEY).asText()).isEqualTo(entityIri.toString());
        assertThat(result.get(EntityConverter.TYPE_KEY).asText()).isEqualTo(entityType.getName());
    }
}
