package edu.stanford.protege.webprotege.match;



import edu.stanford.protege.webprotege.index.AnnotationAssertionAxiomsIndex;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Jun 2018
 */

public class EntityIsDeprecatedMatcher implements Matcher<OWLEntity> {

    private final Matcher<OWLEntity> delegate;

    @Inject
    public EntityIsDeprecatedMatcher(@Nonnull AnnotationAssertionAxiomsIndex axioms) {
        final AnnotationMatcher deprecatedMatcher = new AnnotationMatcher(
                OWLAnnotationProperty::isDeprecated,
                LiteralAnnotationValueMatcher.forIsXsdBooleanTrue()
        );
        delegate = new EntityAnnotationMatcher(axioms,
                                               deprecatedMatcher,
                                               AnnotationPresence.AT_LEAST_ONE);
    }

    @Override
    public boolean matches(@Nonnull OWLEntity value) {
        return delegate.matches(value);
    }
}
