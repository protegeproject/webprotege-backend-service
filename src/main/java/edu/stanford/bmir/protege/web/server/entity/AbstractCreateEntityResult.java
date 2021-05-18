package edu.stanford.bmir.protege.web.server.entity;

import com.google.common.collect.ImmutableCollection;

import edu.stanford.bmir.protege.web.server.dispatch.Result;
import edu.stanford.bmir.protege.web.server.event.EventList;
import edu.stanford.bmir.protege.web.server.event.HasEventList;
import edu.stanford.bmir.protege.web.server.event.ProjectEvent;
import edu.stanford.bmir.protege.web.server.project.HasProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/03/2013
 */
public interface AbstractCreateEntityResult<E extends OWLEntity> extends Result, HasProjectId, HasEventList<ProjectEvent<?>> {

    @Override
    EventList<ProjectEvent<?>> getEventList();

    ImmutableCollection<EntityNode> getEntities();
}
