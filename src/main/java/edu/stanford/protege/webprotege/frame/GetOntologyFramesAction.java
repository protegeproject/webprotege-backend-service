package edu.stanford.protege.webprotege.frame;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.project.ProjectId;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25/07/15
 */
public class GetOntologyFramesAction implements ProjectAction<GetOntologyFramesResult> {

    private ProjectId projectId;

    private GetOntologyFramesAction() {
    }

    private GetOntologyFramesAction(ProjectId projectId) {
        this.projectId = checkNotNull(projectId);
    }

    public static GetOntologyFramesAction create(ProjectId projectId) {
        return new GetOntologyFramesAction(projectId);
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("GetOntologyFramesAction")
                          .addValue(getProjectId())
                          .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getProjectId());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetOntologyFramesAction)) {
            return false;
        }
        GetOntologyFramesAction other = (GetOntologyFramesAction) obj;
        return this.getProjectId().equals(other.getProjectId());
    }
}
