package edu.stanford.protege.webprotege.obo;

import com.google.common.base.Objects;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.common.ProjectId;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Jun 2017
 */
public class GetOboNamespacesAction implements ProjectAction<GetOboNamespacesResult> {

    private ProjectId projectId;


    private GetOboNamespacesAction() {
    }

    private GetOboNamespacesAction(@Nonnull ProjectId projectId) {
        this.projectId = checkNotNull(projectId);
    }

    public static GetOboNamespacesAction getOboNamespaces(ProjectId projectId) {
        return create(projectId);
    }

    public static GetOboNamespacesAction create(@Nonnull ProjectId projectId) {
        return new GetOboNamespacesAction(projectId);
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetOboNamespacesAction)) {
            return false;
        }
        GetOboNamespacesAction other = (GetOboNamespacesAction) obj;
        return this.projectId.equals(other.projectId);
    }


    @Override
    public String toString() {
        return toStringHelper("GetOboNamespacesAction")
                .addValue(projectId)
                .toString();
    }
}
