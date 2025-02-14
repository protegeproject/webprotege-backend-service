package edu.stanford.protege.webprotege.forms.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import edu.stanford.protege.webprotege.MockingUtils;
import edu.stanford.protege.webprotege.forms.data.EntityNameControlData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EntityNameControlDataConverterTest {

    private EntityNameControlDataConverter converter;

    private JsonNodeFactory nodeFactory;

    @Mock
    private EntityNameControlData entityNameControlData;

    @Mock
    private EntityConverter entityConverter;

    private OWLEntity entity;

    @Mock
    private JsonNode mockJsonNode;

    @BeforeEach
    void setUp() {
        converter = new EntityNameControlDataConverter(entityConverter);
        nodeFactory = JsonNodeFactory.instance;
        entity = MockingUtils.mockOWLClass();
    }

    @Test
    void shouldConvertEntityNameControlDataWithEntity() {
        when(entityNameControlData.getEntity()).thenReturn(Optional.of(entity));
        when(entityConverter.convert(entity)).thenReturn(mockJsonNode);
        var result = converter.convert(entityNameControlData);
        assertThat(result).isEqualTo(mockJsonNode);
    }

    @Test
    void shouldConvertEntityNameControlDataWithNoEntity() {
        when(entityNameControlData.getEntity()).thenReturn(Optional.empty());
        var result = converter.convert(entityNameControlData);
        assertThat(result).isEqualTo(nodeFactory.nullNode());
    }
}
