package edu.stanford.protege.webprotege.forms.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import edu.stanford.protege.webprotege.forms.data.ImageControlData;
import org.jetbrains.annotations.NotNull;
import org.semanticweb.owlapi.model.IRI;

public class ImageControlDataConverter {

    private final JsonNodeFactory nodeFactory = JsonNodeFactory.instance;

    public JsonNode convert(@NotNull ImageControlData imageControlData) {
        return imageControlData.getIri()
                .map(IRI::toString)
                .map(String::trim)
                .map(s -> (JsonNode) nodeFactory.textNode(s))
                .orElse(nodeFactory.nullNode());
    }
}
