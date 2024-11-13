package edu.stanford.protege.webprotege.forms.json;

import com.fasterxml.jackson.databind.JsonNode;
import edu.stanford.protege.webprotege.forms.data.ImageControlData;
import edu.stanford.protege.webprotege.forms.field.ImageControlDescriptor;
import org.semanticweb.owlapi.model.IRI;

import java.util.Optional;

public class Json2ImageControlData {

    public Optional<ImageControlData> convert(JsonNode node, ImageControlDescriptor descriptor) {
        if(node.isNull()) {
            return Optional.empty();
        }
        if(!node.isTextual()) {
            return Optional.empty();
        }
        var iriString = node.asText();
        return Optional.of(ImageControlData.get(descriptor, IRI.create(iriString)));
    }
}
