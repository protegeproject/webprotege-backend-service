package edu.stanford.protege.webprotege.forms.json;

import com.fasterxml.jackson.databind.JsonNode;
import edu.stanford.protege.webprotege.forms.data.PrimitiveFormControlData;
import edu.stanford.protege.webprotege.forms.data.SingleChoiceControlData;
import edu.stanford.protege.webprotege.forms.field.ChoiceDescriptor;
import edu.stanford.protege.webprotege.forms.field.DynamicChoiceListSourceDescriptor;
import edu.stanford.protege.webprotege.forms.field.FixedChoiceListSourceDescriptor;
import edu.stanford.protege.webprotege.forms.field.SingleChoiceControlDescriptor;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class Json2SingleChoiceControlData {

    private final Json2Entity json2Entity;

    private final PrimitiveFormControlDataConverter primitiveFormControlDataConverter;

    public Json2SingleChoiceControlData(Json2Entity json2Entity, PrimitiveFormControlDataConverter primitiveFormControlDataConverter) {
        this.json2Entity = json2Entity;
        this.primitiveFormControlDataConverter = primitiveFormControlDataConverter;
    }

    public Optional<SingleChoiceControlData> convert(JsonNode node, @NotNull SingleChoiceControlDescriptor descriptor) {
        var sourceDescriptor = descriptor.getSource();
        if(sourceDescriptor instanceof FixedChoiceListSourceDescriptor fixedChoiceDescriptor) {
            return fixedChoiceDescriptor.getChoices()
                    .stream()
                    .map(ChoiceDescriptor::getValue)
                    .filter(ch -> primitiveFormControlDataConverter.convert(ch).equals(node))
                    .map(d -> SingleChoiceControlData.get(descriptor, d))
                    .findFirst();
        }
        else if(sourceDescriptor instanceof DynamicChoiceListSourceDescriptor dynamicChoiceDescriptor) {
            // All entities
            var entity = json2Entity.convert(node);
            return entity.map(e -> SingleChoiceControlData.get(descriptor, PrimitiveFormControlData.get(e)));
        }
        else {
            return Optional.empty();
        }
    }

}
