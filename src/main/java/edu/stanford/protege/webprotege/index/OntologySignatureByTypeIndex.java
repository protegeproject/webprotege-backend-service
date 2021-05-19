package edu.stanford.protege.webprotege.index;


import edu.stanford.protege.webprotege.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-16
 */
@ProjectSingleton
public interface OntologySignatureByTypeIndex extends Index {

    @Nonnull
    <E extends OWLEntity> Stream<E> getSignature(@Nonnull EntityType<E> type,
                                                 @Nonnull OWLOntologyID ontologyId);
}
