package edu.stanford.protege.webprotege.forms.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import edu.stanford.protege.webprotege.forms.data.MultiChoiceControlData;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class MultiChoiceControlDataConverter {

    private final JsonNodeFactory nodeFactory = JsonNodeFactory.instance;

    private final PrimitiveFormControlDataConverter primitiveFormControlDataConverter;

    public MultiChoiceControlDataConverter(PrimitiveFormControlDataConverter primitiveFormControlDataConverter) {
        this.primitiveFormControlDataConverter = primitiveFormControlDataConverter;
    }

    @Nonnull
    public JsonNode convert(@NotNull MultiChoiceControlData multiChoiceControlData) {
        var arrayNode = nodeFactory.arrayNode();
        multiChoiceControlData.getValues()
                .stream()
                .map(primitiveFormControlDataConverter::convert)
                .forEach(arrayNode::add);
        return arrayNode;
    }
}
