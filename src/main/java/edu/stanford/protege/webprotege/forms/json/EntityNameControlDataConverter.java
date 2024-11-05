package edu.stanford.protege.webprotege.forms.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import edu.stanford.protege.webprotege.forms.data.EntityNameControlData;

import javax.annotation.Nonnull;

public class EntityNameControlDataConverter {

    private final EntityConverter entityConverter;

    private final JsonNodeFactory nodeFactory = JsonNodeFactory.instance;

    public EntityNameControlDataConverter(EntityConverter entityConverter) {
        this.entityConverter = entityConverter;
    }

    @Nonnull
    public JsonNode convert(@Nonnull EntityNameControlData entityNameControlData) {
        return entityNameControlData.getEntity().map(entityConverter::convert).orElse(nodeFactory.nullNode());
    }

}
