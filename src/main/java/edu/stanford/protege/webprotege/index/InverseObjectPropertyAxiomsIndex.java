package edu.stanford.protege.webprotege.index;


import edu.stanford.protege.webprotege.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.OWLInverseObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-22
 */
@ProjectSingleton
public interface InverseObjectPropertyAxiomsIndex extends Index {

    @Nonnull
    Stream<OWLInverseObjectPropertiesAxiom> getInverseObjectPropertyAxioms(@Nonnull OWLObjectProperty property,
                                                                           @Nonnull OWLOntologyID ontologyId);
}
