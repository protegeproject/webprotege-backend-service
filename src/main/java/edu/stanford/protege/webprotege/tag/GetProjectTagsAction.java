package edu.stanford.protege.webprotege.tag;


import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.project.ProjectId;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18 Mar 2018
 */
public class GetProjectTagsAction implements ProjectAction<GetProjectTagsResult> {

    private ProjectId projectId;

    private GetProjectTagsAction(@Nonnull ProjectId projectId) {
        this.projectId = checkNotNull(projectId);
    }


    private GetProjectTagsAction() {
    }

    public static GetProjectTagsAction create(@Nonnull ProjectId projectId) {
        return new GetProjectTagsAction(projectId);
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @Override
    public int hashCode() {
        return projectId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetProjectTagsAction)) {
            return false;
        }
        GetProjectTagsAction other = (GetProjectTagsAction) obj;
        return this.projectId.equals(other.projectId);
    }


    @Override
    public String toString() {
        return toStringHelper("GetProjectTagsAction")
                .addValue(projectId)
                .toString();
    }
}
