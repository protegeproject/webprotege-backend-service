package edu.stanford.protege.webprotege.projectsettings;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import edu.stanford.protege.webprotege.common.Request;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.common.ProjectId;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25/11/14
 */
public class GetProjectSettingsAction implements ProjectAction<GetProjectSettingsResult>, Request<GetProjectSettingsResult> {

    public static final String CHANNEL = "webprotege.projects.GetProjectSettings";

    private final ProjectId projectId;

    private GetProjectSettingsAction(ProjectId projectId) {
        this.projectId = checkNotNull(projectId);
    }

    public static GetProjectSettingsAction create(ProjectId projectId) {
        return new GetProjectSettingsAction(projectId);
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }

    @Nonnull
    @Override
    public ProjectId projectId() {
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
        return this.projectId().equals(other.projectId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectId());
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper("GetProjectSettingsAction")
                          .addValue(projectId())
                          .toString();
    }
}
