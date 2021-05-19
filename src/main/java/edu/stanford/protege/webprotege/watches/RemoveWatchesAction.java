package edu.stanford.protege.webprotege.watches;

import edu.stanford.protege.webprotege.HasUserId;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.project.ProjectId;
import edu.stanford.protege.webprotege.user.UserId;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/03/2013
 */
public class RemoveWatchesAction implements ProjectAction<RemoveWatchesResult>, HasUserId {

    private ProjectId projectId;

    private UserId userId;

    private Set<Watch> watches;


    public RemoveWatchesAction(ProjectId projectId, UserId userId, Set<Watch> watches) {
        this.projectId = checkNotNull(projectId);
        this.watches = new HashSet<Watch>(checkNotNull(watches));
        this.userId = checkNotNull(userId);
    }

    /**
     * For serialization only
     */
    private RemoveWatchesAction() {
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

    public Set<Watch> getWatches() {
        return new HashSet<Watch>(watches);
    }

}
