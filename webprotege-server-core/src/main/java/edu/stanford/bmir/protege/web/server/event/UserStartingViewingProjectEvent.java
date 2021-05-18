package edu.stanford.bmir.protege.web.server.event;



import edu.stanford.bmir.protege.web.server.HasUserId;
import edu.stanford.bmir.protege.web.server.project.ProjectId;
import edu.stanford.bmir.protege.web.server.user.UserId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 27/03/2013
 */
public class UserStartingViewingProjectEvent extends ProjectEvent<UserStartedViewingProjectHandler> implements HasUserId {

    private UserId userId;

    public UserStartingViewingProjectEvent(ProjectId source, UserId userId) {
        super(source);
        this.userId = userId;
    }

    private UserStartingViewingProjectEvent() {

    }

    @Override
    protected void dispatch(UserStartedViewingProjectHandler handler) {
        handler.handleUserStartedViewingProject(this);
    }

    @Override
    public UserId getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UserStartingViewingProjectEvent");
        sb.append("(");
        sb.append(userId);
        sb.append(" ");
        sb.append(getSource());
        sb.append(")");
        return sb.toString();
    }
}
