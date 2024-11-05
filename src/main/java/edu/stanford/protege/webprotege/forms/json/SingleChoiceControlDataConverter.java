package edu.stanford.protege.webprotege.forms.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import edu.stanford.protege.webprotege.forms.data.SingleChoiceControlData;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class SingleChoiceControlDataConverter {

    private final JsonNodeFactory nodeFactory = JsonNodeFactory.instance;

    private final PrimitiveFormControlDataConverter primitiveFormControlDataConverter;

    public SingleChoiceControlDataConverter(PrimitiveFormControlDataConverter primitiveFormControlDataConverter) {
        this.primitiveFormControlDataConverter = primitiveFormControlDataConverter;
    }

    @Nonnull
    public JsonNode convert(@NotNull SingleChoiceControlData singleChoiceControlData) {
        return singleChoiceControlData.getChoice()
                .map(primitiveFormControlDataConverter::convert)
                .orElse(nodeFactory.nullNode());
    }

}
