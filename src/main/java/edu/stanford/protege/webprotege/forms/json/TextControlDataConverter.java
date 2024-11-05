package edu.stanford.protege.webprotege.forms.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import edu.stanford.protege.webprotege.forms.data.TextControlData;
import org.jetbrains.annotations.NotNull;
import org.semanticweb.owlapi.model.OWLLiteral;

public class TextControlDataConverter {

    private final JsonNodeFactory nodeFactory = JsonNodeFactory.instance;

    public JsonNode convert(@NotNull TextControlData textControlData) {
        var val = textControlData.getValue();
        return nodeFactory.textNode(val.map(OWLLiteral::getLiteral).orElse(""));
    }
}
