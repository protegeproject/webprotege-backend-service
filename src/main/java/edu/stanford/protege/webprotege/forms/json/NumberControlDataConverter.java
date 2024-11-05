package edu.stanford.protege.webprotege.forms.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import edu.stanford.protege.webprotege.forms.data.NumberControlData;

import javax.annotation.Nonnull;

public class NumberControlDataConverter {

    private final JsonNodeFactory nodeFactory = JsonNodeFactory.instance;

    private final LiteralConverter literalConverter;

    public NumberControlDataConverter(LiteralConverter literalConverter) {
        this.literalConverter = literalConverter;
    }

    @Nonnull
    public JsonNode convert(NumberControlData numberControlData) {
        var val = numberControlData.getValue();
        return val.map(literalConverter::convert).orElse(nodeFactory.nullNode());
    }
}
