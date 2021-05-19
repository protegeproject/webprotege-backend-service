package edu.stanford.protege.webprotege.index;


import edu.stanford.protege.webprotege.frame.ObjectPropertyCharacteristic;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-10
 */
public interface ObjectPropertyCharacteristicsIndex extends Index {

    boolean hasCharacteristic(@Nonnull
                              OWLObjectProperty property,
                              @Nonnull
                              ObjectPropertyCharacteristic objectPropertyCharacteristic,
                              @Nonnull
                              OWLOntologyID ontologyId);
}
