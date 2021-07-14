package edu.stanford.protege.webprotege.index;

import edu.stanford.protege.webprotege.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-13
 */
@ProjectSingleton
public interface AnnotationAssertionAxiomsByValueIndex extends Index {

    /**
     * Gets the annotation assertion axioms by values, where the value is either an {@link IRI}
     * or an {@link OWLAnonymousIndividual} or an {@link OWLLiteral}.
     * @param value The value
     * @param ontologyId The ontology context.
     */
    @Nonnull
    Stream<OWLAnnotationAssertionAxiom> getAxiomsByValue(@Nonnull OWLAnnotationValue value,
                                                         @Nonnull OWLOntologyID ontologyId);
}
