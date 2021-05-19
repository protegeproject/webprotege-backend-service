package edu.stanford.protege.webprotege.index;


import edu.stanford.protege.webprotege.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-10
 */
@ProjectSingleton
public interface ClassAssertionAxiomsByIndividualIndex extends Index {

    Stream<OWLClassAssertionAxiom> getClassAssertionAxioms(@Nonnull
                                                           OWLIndividual individual,
                                                           @Nonnull
                                                           OWLOntologyID ontologyID);
}
