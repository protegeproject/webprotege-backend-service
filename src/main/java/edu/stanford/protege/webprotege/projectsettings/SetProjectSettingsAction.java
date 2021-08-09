package edu.stanford.protege.webprotege.projectsettings;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.common.ProjectId;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25/11/14
 */
public class SetProjectSettingsAction implements ProjectAction<SetProjectSettingsResult> {

    private ProjectSettings projectSettings;

    /**
     * For serialization purposes only
     */
    private SetProjectSettingsAction() {
    }

    private SetProjectSettingsAction(ProjectSettings projectSettings) {
        this.projectSettings = checkNotNull(projectSettings);
    }

    public static SetProjectSettingsAction create(ProjectSettings projectSettings) {
        return new SetProjectSettingsAction(projectSettings);
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectSettings.getProjectId();
    }

    public ProjectSettings getProjectSettings() {
        return projectSettings;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectSettings);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SetProjectSettingsAction)) {
            return false;
        }
        SetProjectSettingsAction other = (SetProjectSettingsAction) obj;
        return this.projectSettings.equals(other.projectSettings);
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper("SetProjectSettingsAction")
                          .addValue(projectSettings)
                          .toString();
    }
}
