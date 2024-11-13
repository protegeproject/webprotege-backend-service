package edu.stanford.protege.webprotege.forms.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.forms.data.MultiChoiceControlData;
import edu.stanford.protege.webprotege.forms.data.PrimitiveFormControlData;
import edu.stanford.protege.webprotege.forms.field.ChoiceDescriptor;
import edu.stanford.protege.webprotege.forms.field.DynamicChoiceListSourceDescriptor;
import edu.stanford.protege.webprotege.forms.field.FixedChoiceListSourceDescriptor;
import edu.stanford.protege.webprotege.forms.field.MultiChoiceControlDescriptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class Json2MultiChoiceControlData {

    private final Json2Entity json2Entity;

    private final PrimitiveFormControlDataConverter primitiveFormControlDataConverter;

    public Json2MultiChoiceControlData(Json2Entity json2Entity, PrimitiveFormControlDataConverter primitiveFormControlDataConverter) {
        this.json2Entity = json2Entity;
        this.primitiveFormControlDataConverter = primitiveFormControlDataConverter;
    }

    public List<MultiChoiceControlData> convert(ArrayNode node, MultiChoiceControlDescriptor descriptor) {
        // This is an array
        var elementIt = node.elements();
        var choices = new ArrayList<PrimitiveFormControlData>();
        var sourceDescriptor = descriptor.getSource();
        if(sourceDescriptor instanceof FixedChoiceListSourceDescriptor fixedChoiceDescriptor) {
            var choiceMap = new HashMap<JsonNode, PrimitiveFormControlData>();
            fixedChoiceDescriptor.getChoices()
                    .stream()
                    .map(ChoiceDescriptor::getValue)
                    .forEach(ch -> {
                        var choiceNode = primitiveFormControlDataConverter.convert(ch);
                        choiceMap.put(choiceNode, ch);
                    });
            elementIt.forEachRemaining(element -> {
                var data = choiceMap.get(element);
                if(data != null) {
                    choices.add(data);
                }
            });
            return ImmutableList.of(MultiChoiceControlData.get(descriptor, ImmutableList.copyOf(choices)));
        }
        else if(sourceDescriptor instanceof DynamicChoiceListSourceDescriptor dynamicChoiceDescriptor) {
            // All entities
            var entities = new ArrayList<PrimitiveFormControlData>();
            elementIt.forEachRemaining(element -> {
                var entity = json2Entity.convert(element);
                entity.ifPresent(e -> entities.add(PrimitiveFormControlData.get(e)));
            });
            return ImmutableList.of(MultiChoiceControlData.get(descriptor, ImmutableList.copyOf(entities)));
        }
        else {
            return ImmutableList.of();
        }

    }
}
