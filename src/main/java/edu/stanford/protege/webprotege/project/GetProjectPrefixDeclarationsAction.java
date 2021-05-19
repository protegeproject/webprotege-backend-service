package edu.stanford.protege.webprotege.project;


import edu.stanford.protege.webprotege.dispatch.ProjectAction;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27 Feb 2018
 */
public class GetProjectPrefixDeclarationsAction implements ProjectAction<GetProjectPrefixDeclarationsResult> {

    private String projectId;

    private GetProjectPrefixDeclarationsAction(@Nonnull ProjectId projectId) {
        this.projectId = checkNotNull(projectId.getId());
    }


    private GetProjectPrefixDeclarationsAction() {
    }

    public static GetProjectPrefixDeclarationsAction create(@Nonnull ProjectId projectId) {
        return new GetProjectPrefixDeclarationsAction(projectId);
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return ProjectId.get(projectId);
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
        if (!(obj instanceof GetProjectPrefixDeclarationsAction)) {
            return false;
        }
        GetProjectPrefixDeclarationsAction other = (GetProjectPrefixDeclarationsAction) obj;
        return this.projectId.equals(other.projectId);
    }


    @Override
    public String toString() {
        return toStringHelper("GetProjectPrefixDeclarationsAction")
                .addValue(projectId)
                .toString();
    }
}
