package edu.stanford.protege.webprotege.projectsettings;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.project.ProjectId;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25/11/14
 */
public class GetProjectSettingsAction implements ProjectAction<GetProjectSettingsResult> {

    private ProjectId projectId;

    /**
     * For serialization purposes only
     */
    public GetProjectSettingsAction() {
    }

    private GetProjectSettingsAction(ProjectId projectId) {
        this.projectId = checkNotNull(projectId);
    }

    public static GetProjectSettingsAction create(ProjectId projectId) {
        return new GetProjectSettingsAction(projectId);
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetProjectSettingsAction)) {
            return false;
        }
        GetProjectSettingsAction other = (GetProjectSettingsAction) obj;
        return this.getProjectId().equals(other.getProjectId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getProjectId());
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper("GetProjectSettingsAction")
                          .addValue(getProjectId())
                          .toString();
    }
}
