package edu.stanford.protege.webprotege.entity;

import com.google.common.collect.ImmutableCollection;

import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.event.EventList;
import edu.stanford.protege.webprotege.event.HasEventList;
import edu.stanford.protege.webprotege.event.ProjectEvent;
import edu.stanford.protege.webprotege.project.HasProjectId;
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
