package edu.stanford.protege.webprotege.forms.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import edu.stanford.protege.webprotege.forms.data.EntityNameControlData;
import edu.stanford.protege.webprotege.forms.data.FormControlData;
import edu.stanford.protege.webprotege.forms.field.EntityNameControlDescriptor;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.ArrayList;
import java.util.List;
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

    public List<? extends FormControlData> convertAsList(ArrayNode jsonFieldData, EntityNameControlDescriptor entityNameControlDescriptor) {
        List<FormControlData> response = new ArrayList<>();
        for(int i = 0; i < jsonFieldData.size(); i ++) {
            Optional<OWLEntity> entity = json2Entity.convert(jsonFieldData.get(i));
            entity.ifPresent(e -> response.add(EntityNameControlData.get(entityNameControlDescriptor, e)));
        }
        return response;
    }
}
