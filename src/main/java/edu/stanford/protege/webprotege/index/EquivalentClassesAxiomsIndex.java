package edu.stanford.protege.webprotege.index;


import edu.stanford.protege.webprotege.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-09
 */
@ProjectSingleton
public interface EquivalentClassesAxiomsIndex extends Index {

    @Nonnull
    Stream<OWLEquivalentClassesAxiom> getEquivalentClassesAxioms(@Nonnull
                                                                 OWLClass cls,
                                                                 @Nonnull
                                                                 OWLOntologyID ontologyID);
}
