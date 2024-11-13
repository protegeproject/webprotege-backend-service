package edu.stanford.protege.webprotege.forms.json;

import com.fasterxml.jackson.databind.JsonNode;
import edu.stanford.protege.webprotege.DataFactory;
import edu.stanford.protege.webprotege.forms.data.NumberControlData;
import edu.stanford.protege.webprotege.forms.field.NumberControlDescriptor;

import java.util.Optional;

public class Json2NumberControlData {

    public Optional<NumberControlData> convert(JsonNode node, NumberControlDescriptor descriptor) {
        if (node.isNull()) {
            return Optional.empty();
        }
        var value = node.asText("");
        var literal = DataFactory.parseLiteral(value, Optional.empty());
        return Optional.of(NumberControlData.get(descriptor, literal));
    }
}
