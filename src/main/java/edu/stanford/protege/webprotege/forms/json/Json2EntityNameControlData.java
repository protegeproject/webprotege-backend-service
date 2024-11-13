package edu.stanford.protege.webprotege.forms.json;

import com.fasterxml.jackson.databind.JsonNode;
import edu.stanford.protege.webprotege.forms.data.EntityNameControlData;
import edu.stanford.protege.webprotege.forms.field.EntityNameControlDescriptor;

import java.util.Optional;

public class Json2EntityNameControlData {

    private final Json2Entity json2Entity;

    public Json2EntityNameControlData(Json2Entity json2Entity) {
        this.json2Entity = json2Entity;
    }

    public Optional<EntityNameControlData> convert(JsonNode node, EntityNameControlDescriptor descriptor) {
        if(node.isNull()) {
            return Optional.empty();
        }
        var entity = json2Entity.convert(node);
        return entity.map(e -> EntityNameControlData.get(descriptor, e));
    }
}
