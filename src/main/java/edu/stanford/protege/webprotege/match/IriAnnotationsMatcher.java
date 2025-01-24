package edu.stanford.protege.webprotege.match;



import edu.stanford.protege.webprotege.index.AnnotationAssertionAxiomsIndex;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Jun 2018
 */

public class IriAnnotationsMatcher implements Matcher<IRI> {

    @Nonnull
    private final AnnotationAssertionAxiomsIndex axiomProvider;

    @Nonnull
    private final Matcher<OWLAnnotation> annotationMatcher;

    @Inject
    public IriAnnotationsMatcher(@Nonnull AnnotationAssertionAxiomsIndex axiomProvider,
                                 @Nonnull Matcher<OWLAnnotation> annotationMatcher) {
        this.axiomProvider = checkNotNull(axiomProvider);
        this.annotationMatcher = checkNotNull(annotationMatcher);
    }

    @Override
    public boolean matches(@Nonnull IRI subject) {
        return axiomProvider.getAnnotationAssertionAxioms(subject)
                            .map(OWLAnnotationAssertionAxiom::getAnnotation)
                            .anyMatch(annotationMatcher::matches);
    }
}
