package edu.stanford.protege.webprotege.forms;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import edu.stanford.protege.webprotege.forms.data.PrimitiveFormControlData;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 1 Jul 2017
 */
@JsonComponent
public class FormControlValueDeserializer extends StdDeserializer<PrimitiveFormControlData> {

    private final OWLDataFactory dataFactory;

    public FormControlValueDeserializer(OWLDataFactory dataFactory) {
        super(PrimitiveFormControlData.class);
        this.dataFactory = dataFactory;
    }

    @Override
    public PrimitiveFormControlData deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

        JsonNode node = p.readValueAsTree();
        if (node.isTextual()) {
            return PrimitiveFormControlData.get(node.asText());
        }
        else if (node.isNumber()) {
            return PrimitiveFormControlData.get(node.asDouble());
        }
        else if (node.isBoolean()) {
            return PrimitiveFormControlData.get(node.asBoolean());
        }
        else if (node.isObject()) {
            if (node.has("iri")) {
                if (node.has("type")) {
                    IRI iri = IRI.create(node.get("iri").asText());
                    String type = node.get("type").asText();
                    switch (type) {
                        case "owl:Class":
                            return PrimitiveFormControlData.get(dataFactory.getOWLClass(iri));
                        case "owl:ObjectProperty":
                            return PrimitiveFormControlData.get(dataFactory.getOWLObjectProperty(iri));
                        case "owl:DatatypeProperty":
                            return PrimitiveFormControlData.get(dataFactory.getOWLDataProperty(iri));
                        case "owl:AnnotationProperty":
                            return PrimitiveFormControlData.get(dataFactory.getOWLAnnotationProperty(iri));
                        case "rdfs:Datatype":
                            return PrimitiveFormControlData.get(dataFactory.getOWLDatatype(iri));
                        case "owl:NamedIndividual":
                            return PrimitiveFormControlData.get(dataFactory.getOWLNamedIndividual(iri));
                    }
                    throw new JsonParseException(p, "Unrecognised term type: " + type);
                }
                else {
                    return PrimitiveFormControlData.get(IRI.create(node.get("iri").asText()));
                }
            }
            // literal supported for legacy reasons
            else if (node.has("value") || node.has("literal")) {
                JsonNode literalNode = node.get("value");
                if (literalNode == null) {
                    literalNode = node.get("literal");
                }
                String literal = literalNode.asText();
                JsonNode langNode = node.get("lang");
                JsonNode typeNode = node.get("type");
                if (typeNode != null && !typeNode.asText()
                                                 .equals(OWLRDFVocabulary.RDF_PLAIN_LITERAL.getIRI().toString())) {
                    return PrimitiveFormControlData.get(dataFactory.getOWLLiteral(literal,
                                                                                  dataFactory.getOWLDatatype(IRI.create(
                                                                                          typeNode.asText()))));
                }
                else {
                    return PrimitiveFormControlData.get(dataFactory.getOWLLiteral(literal, langNode.asText("")));
                }

            }
        }
        throw new JsonParseException(p, "Cannot parse node as primitive value");
    }
}
