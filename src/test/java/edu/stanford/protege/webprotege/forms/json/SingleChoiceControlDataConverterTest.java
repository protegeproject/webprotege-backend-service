package edu.stanford.protege.webprotege.forms.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import edu.stanford.protege.webprotege.forms.data.PrimitiveFormControlData;
import edu.stanford.protege.webprotege.forms.data.SingleChoiceControlData;
import edu.stanford.protege.webprotege.forms.json.SingleChoiceControlDataConverter;
import edu.stanford.protege.webprotege.forms.json.PrimitiveFormControlDataConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SingleChoiceControlDataConverterTest {

    private SingleChoiceControlDataConverter converter;

    private JsonNodeFactory nodeFactory;

    @Mock
    private SingleChoiceControlData singleChoiceControlData;

    @Mock
    private PrimitiveFormControlDataConverter primitiveFormControlDataConverter;

    @Mock
    private JsonNode mockJsonNode;

    @BeforeEach
    void setUp() {
        converter = new SingleChoiceControlDataConverter(primitiveFormControlDataConverter);
        nodeFactory = JsonNodeFactory.instance;
    }

    @Test
    void shouldConvertSingleChoiceControlDataWithChoice() {
        var choice = PrimitiveFormControlData.get("TestValue");
        when(singleChoiceControlData.getChoice()).thenReturn(Optional.of(choice));
        when(primitiveFormControlDataConverter.convert(choice)).thenReturn(mockJsonNode);
        var result = converter.convert(singleChoiceControlData);
        assertThat(result).isEqualTo(mockJsonNode);
    }

    @Test
    void shouldConvertSingleChoiceControlDataWithEmptyChoice() {
        when(singleChoiceControlData.getChoice()).thenReturn(Optional.empty());
        var result = converter.convert(singleChoiceControlData);
        assertThat(result).isEqualTo(nodeFactory.nullNode());
    }
}