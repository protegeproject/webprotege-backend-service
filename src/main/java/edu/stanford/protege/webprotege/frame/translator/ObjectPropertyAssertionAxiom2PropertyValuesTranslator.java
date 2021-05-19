package edu.stanford.protege.webprotege.frame.translator;

import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.frame.PlainPropertyIndividualValue;
import edu.stanford.protege.webprotege.frame.PlainPropertyValue;
import edu.stanford.protege.webprotege.frame.State;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-02
 */
public class ObjectPropertyAssertionAxiom2PropertyValuesTranslator {

    @Inject
    public ObjectPropertyAssertionAxiom2PropertyValuesTranslator() {
    }

    @Nonnull
    public Set<PlainPropertyValue> translate(@Nonnull OWLObjectPropertyAssertionAxiom axiom,
                                              @Nonnull OWLEntity subject,
                                              @Nonnull State initialState) {
        if(axiom.getProperty().isAnonymous()) {
            return ImmutableSet.of();
        }
        if(axiom.getObject().isAnonymous()) {
            return ImmutableSet.of();
        }
        if(!axiom.getSubject().equals(subject)) {
            return ImmutableSet.of();
        }
        var property = axiom.getProperty().asOWLObjectProperty();
        var object = axiom.getObject().asOWLNamedIndividual();
        return ImmutableSet.of(PlainPropertyIndividualValue.get(property,
                                                                object,
                                                                 initialState));
    }
}
