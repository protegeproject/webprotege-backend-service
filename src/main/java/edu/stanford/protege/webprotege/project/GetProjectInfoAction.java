package edu.stanford.protege.webprotege.project;


import edu.stanford.protege.webprotege.dispatch.ProjectAction;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21 Aug 2018
 */
public class GetProjectInfoAction implements ProjectAction<GetProjectInfoResult> {

    private ProjectId projectId;


    private GetProjectInfoAction() {
    }

    private GetProjectInfoAction(@Nonnull ProjectId projectId) {
        this.projectId = checkNotNull(projectId);
    }

    public static GetProjectInfoAction create(@Nonnull ProjectId projectId) {
        return new GetProjectInfoAction(projectId);
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
        if (!(obj instanceof GetProjectInfoAction)) {
            return false;
        }
        GetProjectInfoAction other = (GetProjectInfoAction) obj;
        return this.projectId.equals(other.projectId);
    }


    @Override
    public String toString() {
        return toStringHelper("GetProjectInfoAction")
                .addValue(projectId)
                .toString();
    }
}
