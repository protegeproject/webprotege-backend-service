package edu.stanford.protege.webprotege.forms.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.semanticweb.owlapi.model.IRI;

public class IriConverter {

    public static final String IRI_KEY = "@id";

    private final JsonNodeFactory nodeFactory = JsonNodeFactory.instance;

    private final JsonNameExtractor nameExtractor;

    public IriConverter(JsonNameExtractor nameExtractor) {
        this.nameExtractor = nameExtractor;
    }

    public JsonNode convert(IRI iri) {
        var override = nameExtractor.getJsonName(iri);
        if (override.isPresent()) {
            return nodeFactory.textNode(override.get());
        }
        var iriNode = nodeFactory.objectNode();
        iriNode.set(IRI_KEY, nodeFactory.textNode(iri.toString()));
        return iriNode;
    }
}
