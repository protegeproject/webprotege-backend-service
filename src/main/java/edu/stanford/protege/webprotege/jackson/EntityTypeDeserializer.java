package edu.stanford.protege.webprotege.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import edu.stanford.protege.webprotege.entity.Entity;
import org.semanticweb.owlapi.model.EntityType;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18 Jun 2018
 */
public class EntityTypeDeserializer extends StdDeserializer<EntityType<?>> {

    public EntityTypeDeserializer() {
        super(EntityType.class);
    }

    @Override
    public EntityType<?> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String typeName = jsonParser.getValueAsString();
        if(isEntityType(typeName, EntityType.CLASS)) {
            return EntityType.CLASS;
        }
        else if(isEntityType(typeName, EntityType.OBJECT_PROPERTY)) {
            return EntityType.OBJECT_PROPERTY;
        }
        else if(isEntityType(typeName, EntityType.DATA_PROPERTY)) {
            return EntityType.DATA_PROPERTY;
        }
        else if(isEntityType(typeName, EntityType.ANNOTATION_PROPERTY)) {
            return EntityType.ANNOTATION_PROPERTY;
        }
        else if(isEntityType(typeName, EntityType.NAMED_INDIVIDUAL)) {
            return EntityType.NAMED_INDIVIDUAL;
        }
        else if(isEntityType(typeName, EntityType.DATATYPE)) {
            return EntityType.DATATYPE;
        }
        throw new IOException("Unrecognized entity type name: " + typeName);
    }

    private static boolean isEntityType(String typeName, EntityType<?> entityType) {
        return entityType.getPrefixedName().equals(typeName)
                || entityType.getName().equals(typeName);
    }
}
