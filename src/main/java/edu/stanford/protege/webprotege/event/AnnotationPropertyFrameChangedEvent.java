package edu.stanford.protege.webprotege.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.stanford.protege.webprotege.project.ProjectId;
import edu.stanford.protege.webprotege.user.UserId;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 18/12/2012
 */
public class AnnotationPropertyFrameChangedEvent extends EntityFrameChangedEvent<OWLAnnotationProperty, AnnotationPropertyFrameChangedEventHandler> {

    @JsonCreator
    public AnnotationPropertyFrameChangedEvent(@JsonProperty("entity") OWLAnnotationProperty entity,
                                               @JsonProperty("projectId") ProjectId projectId,
                                               @JsonProperty("userId") UserId userId) {
        super(entity, projectId, userId);
    }

    @Override
    protected void dispatch(AnnotationPropertyFrameChangedEventHandler handler) {
        handler.annotationPropertyFrameChanged(this);
    }
}
