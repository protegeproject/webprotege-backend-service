package edu.stanford.protege.webprotege.index;

import edu.stanford.protege.webprotege.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-07
 */
@ProjectSingleton
public interface AxiomsByEntityReferenceIndex extends Index {

    Stream<OWLAxiom> getReferencingAxioms(@Nonnull
                                          OWLEntity entity,
                                          @Nonnull
                                          OWLOntologyID ontologyId);
}
