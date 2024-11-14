package edu.stanford.protege.webprotege.forms.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.forms.data.MultiChoiceControlData;
import edu.stanford.protege.webprotege.forms.data.PrimitiveFormControlData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MultiChoiceControlDataConverterTest {

    private MultiChoiceControlDataConverter converter;

    private JsonNodeFactory nodeFactory;

    @Mock
    private MultiChoiceControlData multiChoiceControlData;

    @Mock
    private PrimitiveFormControlDataConverter primitiveFormControlDataConverter;

    @Mock
    private JsonNode mockJsonNode1;

    @Mock
    private JsonNode mockJsonNode2;

    @BeforeEach
    void setUp() {
        converter = new MultiChoiceControlDataConverter(primitiveFormControlDataConverter);
        nodeFactory = JsonNodeFactory.instance;
    }

    @Test
    void shouldConvertMultiChoiceControlDataWithMultipleChoices() {
        // Arrange
        var choice1 = PrimitiveFormControlData.get("TestValueA");
        var choice2 = PrimitiveFormControlData.get("TestValueB");
        when(multiChoiceControlData.getValues()).thenReturn(ImmutableList.of(choice1, choice2));
        when(primitiveFormControlDataConverter.convert(choice1)).thenReturn(mockJsonNode1);
        when(primitiveFormControlDataConverter.convert(choice2)).thenReturn(mockJsonNode2);

        var result = converter.convert(multiChoiceControlData);

        assertThat(result).isInstanceOf(ArrayNode.class);
        assertThat(result).hasSize(2);
        assertThat(result.get(0)).isEqualTo(mockJsonNode1);
        assertThat(result.get(1)).isEqualTo(mockJsonNode2);
    }

    @Test
    void shouldConvertMultiChoiceControlDataWithNoChoices() {
        when(multiChoiceControlData.getValues()).thenReturn(ImmutableList.of());
        var result = converter.convert(multiChoiceControlData);
        assertThat(result).isInstanceOf(ArrayNode.class);
        assertThat(result).isEmpty();
    }
}
