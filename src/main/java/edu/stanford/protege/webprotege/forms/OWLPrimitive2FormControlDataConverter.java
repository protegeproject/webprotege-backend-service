package edu.stanford.protege.webprotege.forms;

import edu.stanford.protege.webprotege.forms.data.PrimitiveFormControlData;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-01
 */
public class OWLPrimitive2FormControlDataConverter {

    @Nonnull
    private final OWLEntity2FormControlDataVisitor entityVisitor;

    @Inject
    public OWLPrimitive2FormControlDataConverter(@Nonnull OWLEntity2FormControlDataVisitor entityVisitor) {
        this.entityVisitor = entityVisitor;
    }

    @Nonnull
    public PrimitiveFormControlData toFormControlData(@Nonnull OWLPrimitive primitive) {
        if(primitive instanceof OWLEntity) {
            return ((OWLEntity) primitive).accept(entityVisitor);
        }
        else if(primitive instanceof IRI) {
            return PrimitiveFormControlData.get((IRI) primitive);
        }
        else if(primitive instanceof OWLLiteral) {
            return PrimitiveFormControlData.get((OWLLiteral) primitive);
        }
        else if(primitive instanceof OWLAnonymousIndividual) {
            throw new RuntimeException("Anonymous individuals are not supported");
        }
        else {
            throw new RuntimeException("Missing case for primitive type " + primitive.getClass().getName());
        }
    }
}
