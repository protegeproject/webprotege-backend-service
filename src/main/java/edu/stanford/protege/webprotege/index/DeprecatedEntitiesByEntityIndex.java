package edu.stanford.protege.webprotege.index;


import edu.stanford.protege.webprotege.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-15
 */
@ProjectSingleton
public interface DeprecatedEntitiesByEntityIndex extends Index {

    boolean isDeprecated(@Nonnull OWLEntity entity);
}
