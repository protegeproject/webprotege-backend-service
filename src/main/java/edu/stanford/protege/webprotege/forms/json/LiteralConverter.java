package edu.stanford.protege.webprotege.forms.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LiteralConverter {

    private final Logger logger = LoggerFactory.getLogger(LiteralConverter.class);

    private final JsonNodeFactory nodeFactory = JsonNodeFactory.instance;

    public JsonNode convert(OWLLiteral value) {
        try {
            var datatype = value.getDatatype();
            if (isIntegerDatatype(datatype)) {
                var intValue = Integer.parseInt(value.getLiteral());
                return nodeFactory.numberNode(intValue);
            } else if (isUnsignedIntDatatype(datatype)) {
                var intValue = Integer.parseUnsignedInt(value.getLiteral());
                return nodeFactory.numberNode(intValue);
            } else if(isLongDatatype(datatype)) {
                var longValue = Long.parseLong(value.getLiteral());
                return nodeFactory.numberNode(longValue);
            } else if(isUnsignedLongDatatype(datatype)) {
                var longValue = Long.parseLong(value.getLiteral());
                return nodeFactory.numberNode(longValue);
            }
            else if (isDoubleDatatype(datatype)) {
                var doubleValue = Double.parseDouble(value.getLiteral());
                return nodeFactory.numberNode(doubleValue);
            } else if (isFloatDatatype(datatype)) {
                var floatValue = Float.parseFloat(value.getLiteral());
                return nodeFactory.numberNode(floatValue);
            }
        } catch (NumberFormatException e) {
            logger.debug("Error parsing {} into number: {}", value.getLiteral(), e.getMessage());
        }
        return nodeFactory.textNode(value.getLiteral());
    }

    private static boolean isFloatDatatype(OWLDatatype datatype) {
        return OWL2Datatype.XSD_FLOAT.getIRI().equals(datatype.getIRI());
    }

    private static boolean isDoubleDatatype(OWLDatatype datatype) {
        return OWL2Datatype.XSD_DECIMAL.getIRI().equals(datatype.getIRI()) || OWL2Datatype.XSD_DOUBLE.getIRI().equals(datatype.getIRI());
    }

    private static boolean isUnsignedLongDatatype(OWLDatatype datatype) {
        return OWL2Datatype.XSD_UNSIGNED_LONG.getIRI().equals(datatype.getIRI());
    }

    private static boolean isLongDatatype(OWLDatatype datatype) {
        return OWL2Datatype.XSD_LONG.getIRI().equals(datatype.getIRI());
    }

    private static boolean isUnsignedIntDatatype(OWLDatatype datatype) {
        return OWL2Datatype.XSD_UNSIGNED_INT.getIRI().equals(datatype.getIRI())
                || OWL2Datatype.XSD_UNSIGNED_SHORT.getIRI().equals(datatype.getIRI())
                || OWL2Datatype.XSD_UNSIGNED_BYTE.getIRI().equals(datatype);
    }

    private static boolean isIntegerDatatype(OWLDatatype datatype) {
        return OWL2Datatype.XSD_INT.getIRI().equals(datatype.getIRI())
                || OWL2Datatype.XSD_INTEGER.getIRI().equals(datatype.getIRI())
                || OWL2Datatype.XSD_SHORT.getIRI().equals(datatype.getIRI())
                || OWL2Datatype.XSD_BYTE.getIRI().equals(datatype);
    }
}
