package edu.stanford.protege.webprotege.index;


import edu.stanford.protege.webprotege.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.OWLDifferentIndividualsAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-22
 */
@ProjectSingleton
public interface DifferentIndividualsAxiomsIndex extends Index {

    @Nonnull
    Stream<OWLDifferentIndividualsAxiom> getDifferentIndividualsAxioms(@Nonnull OWLIndividual individual,
                                                                       @Nonnull OWLOntologyID ontologyId);
}
