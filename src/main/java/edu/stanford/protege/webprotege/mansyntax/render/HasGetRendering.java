package edu.stanford.protege.webprotege.mansyntax.render;

import edu.stanford.protege.webprotege.entity.OWLEntityData;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Apr 2017
 */
public interface HasGetRendering {

    OWLEntityData getRendering(OWLEntity entity);
}
