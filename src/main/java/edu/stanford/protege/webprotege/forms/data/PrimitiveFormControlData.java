package edu.stanford.protege.webprotege.forms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLPrimitive;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import uk.ac.manchester.cs.owl.owlapi.OWL2DatatypeImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLLiteralImplBoolean;
import uk.ac.manchester.cs.owl.owlapi.OWLLiteralImplDouble;
import uk.ac.manchester.cs.owl.owlapi.OWLLiteralImplString;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-08
 */
@JsonSerialize(converter = PrimitiveFormControlData2ProxyConverter.class)
@JsonDeserialize(converter = Proxy2PrimitiveFormControlDataConverter.class)
public interface PrimitiveFormControlData {

    static PrimitiveFormControlData get(OWLEntity entity) {
        return EntityFormControlData.get(entity);
    }

    static PrimitiveFormControlData get(IRI iri) {
        return IriFormControlData.get(iri);
    }

    static PrimitiveFormControlData get(OWLLiteral literal) {
        return LiteralFormControlData.get(literal);
    }

    static PrimitiveFormControlData get(String text) {
        return LiteralFormControlData.get(new OWLLiteralImplString(text));
    }

    static PrimitiveFormControlData get(double value) {
        return LiteralFormControlData.get(new OWLLiteralImplDouble(value,
                                                                   new OWL2DatatypeImpl(OWL2Datatype.XSD_DECIMAL)));
    }

    static PrimitiveFormControlData get(boolean value) {
        return LiteralFormControlData.get(new OWLLiteralImplBoolean(value,
                                                                    new OWL2DatatypeImpl(OWL2Datatype.XSD_BOOLEAN)));
    }

    @Nonnull
    Optional<OWLEntity> asEntity();

    @Nonnull
    Optional<IRI> asIri();

    @Nonnull
    Optional<OWLLiteral> asLiteral();

    @JsonIgnore
    @Nonnull
    OWLPrimitive getPrimitive();

    PrimitiveFormControlDataProxy toPrimitiveFormControlDataProxy();
}
