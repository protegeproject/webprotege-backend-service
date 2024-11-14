package edu.stanford.protege.webprotege.forms.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;

public class EntityConverter {

    public static final String IRI_KEY = "@id";

    public static final String TYPE_KEY = "@type";

    private final JsonNodeFactory nodeFactory = JsonNodeFactory.instance;

    private final JsonNameExtractor nameExtractor;

    public EntityConverter(JsonNameExtractor nameExtractor) {
        this.nameExtractor = nameExtractor;
    }

    @Nonnull
    public JsonNode convert(OWLEntity entity) {
        var entityIri = entity.getIRI();
        var override = nameExtractor.getJsonName(entityIri);
        if (override.isPresent()) {
            return nodeFactory.textNode(override.get());
        } else {
            var entityNode = nodeFactory.objectNode();
            entityNode.set(IRI_KEY, nodeFactory.textNode(entityIri.toString()));
            entityNode.set(TYPE_KEY, nodeFactory.textNode(entity.getEntityType().getName()));
            return entityNode;
        }
    }
}
