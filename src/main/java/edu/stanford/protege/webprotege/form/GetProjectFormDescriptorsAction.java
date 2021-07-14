package edu.stanford.protege.webprotege.form;

import com.google.common.base.MoreObjects;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.project.ProjectId;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-20
 */
public class GetProjectFormDescriptorsAction implements ProjectAction<GetProjectFormDescriptorsResult> {

    private ProjectId projectId;

    private GetProjectFormDescriptorsAction(ProjectId projectId) {
        this.projectId = checkNotNull(projectId);
    }


    private GetProjectFormDescriptorsAction() {
    }

    public static GetProjectFormDescriptorsAction create(ProjectId projectId) {
        return new GetProjectFormDescriptorsAction(projectId);
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
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof GetProjectFormDescriptorsAction)) {
            return false;
        }
        GetProjectFormDescriptorsAction other = (GetProjectFormDescriptorsAction) obj;
        return this.projectId.equals(other.projectId);
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper("GetProjectFormDescriptorsAction")
                .addValue(projectId)
                .toString();
    }
}
