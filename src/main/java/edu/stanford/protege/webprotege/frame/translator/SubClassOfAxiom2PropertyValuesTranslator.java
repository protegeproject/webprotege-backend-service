package edu.stanford.protege.webprotege.frame.translator;

import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.frame.PlainPropertyValue;
import edu.stanford.protege.webprotege.frame.State;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-02
 */
public class SubClassOfAxiom2PropertyValuesTranslator {

    private final ClassExpression2PropertyValuesTranslator classExpression2PropertyValuesTranslator;

    @Inject
    public SubClassOfAxiom2PropertyValuesTranslator(ClassExpression2PropertyValuesTranslator classExpression2PropertyValuesTranslator) {
        this.classExpression2PropertyValuesTranslator = classExpression2PropertyValuesTranslator;
    }

    @Nonnull
    public Set<PlainPropertyValue> translate(@Nonnull OWLSubClassOfAxiom axiom,
                                             @Nonnull OWLEntity subject,
                                             @Nonnull State state) {
        if(!axiom.getSubClass().equals(subject)) {
            return ImmutableSet.of();
        }
        var superClass = axiom.getSuperClass();
        return classExpression2PropertyValuesTranslator.translate(state, superClass);
    }
}
