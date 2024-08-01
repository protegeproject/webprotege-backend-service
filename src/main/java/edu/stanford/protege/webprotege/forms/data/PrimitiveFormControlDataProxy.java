package edu.stanford.protege.webprotege.forms.data;

import com.fasterxml.jackson.annotation.*;
import org.semanticweb.owlapi.model.*;
import uk.ac.manchester.cs.owl.owlapi.*;

import javax.annotation.Nullable;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-11-09
 */
public record PrimitiveFormControlDataProxy(@JsonInclude(JsonInclude.Include.NON_EMPTY) @Nullable String iri,
                                            @JsonInclude(JsonInclude.Include.NON_EMPTY) @JsonProperty("@type") @Nullable String type,
                                            @JsonInclude(JsonInclude.Include.NON_EMPTY) @Nullable String value,
                                            @JsonInclude(JsonInclude.Include.NON_EMPTY) @JsonProperty("type") @Nullable String datatype,
                                            @JsonInclude(JsonInclude.Include.NON_EMPTY) @Nullable String lang) {

    public PrimitiveFormControlData toPrimitiveFormControlData() {
        if(type != null) {
            if(iri == null) {
                throw new RuntimeException("Missing iri");
            }
            if(EntityType.CLASS.getName().equals(type) || EntityType.CLASS.getPrefixedName().equals(type)) {
                return PrimitiveFormControlData.get(new OWLClassImpl(IRI.create(iri)));
            }
            else if(EntityType.OBJECT_PROPERTY.getName().equals(type) || EntityType.OBJECT_PROPERTY.getPrefixedName().equals(type)) {
                return PrimitiveFormControlData.get(new OWLObjectPropertyImpl(IRI.create(iri)));

            }
            else if(EntityType.DATA_PROPERTY.getName().equals(type) || EntityType.DATA_PROPERTY.getPrefixedName().equals(type)) {
                return PrimitiveFormControlData.get(new OWLDataPropertyImpl(IRI.create(iri)));

            }
            else if(EntityType.ANNOTATION_PROPERTY.getName().equals(type) || EntityType.ANNOTATION_PROPERTY.getPrefixedName().equals(type)) {
                return PrimitiveFormControlData.get(new OWLAnnotationPropertyImpl(IRI.create(iri)));

            }
            else if(EntityType.DATATYPE.getName().equals(type) || EntityType.DATATYPE.getPrefixedName().equals(type)) {
                return PrimitiveFormControlData.get(new OWLDatatypeImpl(IRI.create(iri)));
            }
            else if(EntityType.NAMED_INDIVIDUAL.getName().equals(type) || EntityType.NAMED_INDIVIDUAL.getPrefixedName().equals(type)) {
                return PrimitiveFormControlData.get(new OWLNamedIndividualImpl(IRI.create(iri)));
            }
            else {
                throw new RuntimeException("Unrecongized entity type: " + type);
            }

        }
        else if(iri != null) {
            return PrimitiveFormControlData.get(IRI.create(iri));
        }
        else if(value != null) {
            OWLDatatype dt = null;
            if(this.datatype != null) {
                dt = new OWLDatatypeImpl(IRI.create(this.datatype));
            }
            return PrimitiveFormControlData.get(new OWLLiteralImpl(this.value, this.lang, dt));
        }
        else {
            throw new RuntimeException("Expected @type, iri or value");
        }
    }
}
