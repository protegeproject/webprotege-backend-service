package edu.stanford.protege.webprotege.forms.json;

import com.fasterxml.jackson.databind.JsonNode;
import edu.stanford.protege.webprotege.forms.data.TextControlData;
import edu.stanford.protege.webprotege.forms.field.StringType;
import edu.stanford.protege.webprotege.forms.field.TextControlDescriptor;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.vocab.OWL2Datatype;

import java.util.Optional;

public class Json2TextControlData {

    private final OWLDataFactory dataFactory;

    public Json2TextControlData(OWLDataFactory dataFactory) {
        this.dataFactory = dataFactory;
    }

    /**
     * Converts a JSON node representation of {@link TextControlData} into {@link TextControlData}.
     * @param node The node
     * @param descriptor The descriptor.  This will determine treatment of language tags.
     * @return The {@link TextControlData} of the specified node, if it is well-formed.
     */
    public Optional<TextControlData> convert(JsonNode node, TextControlDescriptor descriptor) {
        if(!node.isTextual()) {
            return Optional.empty();
        }
        var stringType = descriptor.getStringType();
        if(stringType.equals(StringType.LANG_STRING)) {
            var value = dataFactory.getOWLLiteral(node.textValue(), OWL2Datatype.RDF_PLAIN_LITERAL);
            return Optional.of(TextControlData.get(descriptor, value));
        }
        else if(stringType.equals(StringType.SPECIFIC_LANG_STRING)) {
            var langTag = descriptor.getSpecificLangTag();
            var value = dataFactory.getOWLLiteral(node.textValue(), langTag);
            return Optional.of(TextControlData.get(descriptor, value));
        }
        else {
            // Just xsd:string
            var value = dataFactory.getOWLLiteral(node.textValue());
            return Optional.of(TextControlData.get(descriptor, value));
        }
    }

}
