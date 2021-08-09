package edu.stanford.protege.webprotege.watches;

import edu.stanford.protege.webprotege.HasUserId;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.user.UserId;

import javax.annotation.Nonnull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/03/2013
 */
public class AddWatchAction implements ProjectAction<AddWatchResult>, HasUserId {

    private Watch watch;

    private ProjectId projectId;

    private UserId userId;

    public AddWatchAction(Watch watch, ProjectId projectId, UserId userId) {
        this.watch = watch;
        this.projectId = projectId;
        this.userId = userId;
    }

    private AddWatchAction() {
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @Override
    public UserId getUserId() {
        return userId;
    }

    public Watch getWatch() {
        return watch;
    }
}
