package edu.stanford.protege.webprotege.entity;

import edu.stanford.protege.webprotege.common.*;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.Set;

public interface EntityStatusManager {
    Set<EntityStatus> getEntityStatuses(OWLEntity entity);
}
