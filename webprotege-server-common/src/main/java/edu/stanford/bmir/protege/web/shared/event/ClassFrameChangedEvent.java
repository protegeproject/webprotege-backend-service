package edu.stanford.bmir.protege.web.shared.event;


import com.google.common.base.MoreObjects;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.OWLClass;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 18/12/2012
 */
public class ClassFrameChangedEvent extends EntityFrameChangedEvent<OWLClass, ClassFrameChangedEventHandler> {

    public ClassFrameChangedEvent(OWLClass entity, ProjectId projectId, UserId userId) {
        super(entity, projectId, userId);
    }

    private ClassFrameChangedEvent() {
        super();
    }

    @Override
    protected void dispatch(ClassFrameChangedEventHandler handler) {
        handler.classFrameChanged(this);
    }

    @Override
    public int hashCode() {
        return "ClassFrameChangedEvent".hashCode() + getEntity().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof ClassFrameChangedEvent)) {
            return false;
        }
        ClassFrameChangedEvent other = (ClassFrameChangedEvent) obj;
        return this.getEntity().equals(other.getEntity());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("ClassFrameChangedEvent")
                          .addValue(getEntity()).toString();
    }
}
