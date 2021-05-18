package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.server.entity.OWLEntityData;

import java.util.Set;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 20/03/2014
 */
public interface HasFreshEntities {

    Set<OWLEntityData> getFreshEntities();
}
