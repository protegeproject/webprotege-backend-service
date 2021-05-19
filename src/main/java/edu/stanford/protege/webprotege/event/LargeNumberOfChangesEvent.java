package edu.stanford.protege.webprotege.event;



import edu.stanford.protege.webprotege.project.ProjectId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-11-04
 */
public class LargeNumberOfChangesEvent extends ProjectEvent<LargeNumberOfChangesHandler> {

    public LargeNumberOfChangesEvent(ProjectId source) {
        super(source);
    }


    private LargeNumberOfChangesEvent() {
    }

    @Override
    protected void dispatch(LargeNumberOfChangesHandler handler) {
        handler.handleLargeNumberOfChanges(this);
    }

    @Override
    public int hashCode() {
        return getProjectId().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof LargeNumberOfChangesEvent)) {
            return false;
        }
        LargeNumberOfChangesEvent other = (LargeNumberOfChangesEvent) obj;
        return this.getProjectId().equals(other.getProjectId());
    }
}
