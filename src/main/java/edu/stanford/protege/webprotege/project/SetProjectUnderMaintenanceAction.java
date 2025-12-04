package edu.stanford.protege.webprotege.project;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.common.ProjectId;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Action to set the under maintenance status of a project.
 */
public class SetProjectUnderMaintenanceAction implements ProjectAction<SetProjectUnderMaintenanceResult> {

    public static final String CHANNEL = "webprotege.projects.SetProjectUnderMaintenance";

    private ProjectId projectId;
    private boolean underMaintenance;

    @Override
    public String getChannel() {
        return CHANNEL;
    }

    private SetProjectUnderMaintenanceAction(ProjectId projectId, boolean underMaintenance) {
        this.projectId = checkNotNull(projectId);
        this.underMaintenance = underMaintenance;
    }

    public SetProjectUnderMaintenanceAction(){

    }

    @JsonCreator
    public static SetProjectUnderMaintenanceAction create(@JsonProperty("projectId") ProjectId projectId,
                                                          @JsonProperty("underMaintenance") boolean underMaintenance) {
        return new SetProjectUnderMaintenanceAction(projectId, underMaintenance);
    }

    @Nonnull
    @Override
    public ProjectId projectId() {
        return projectId;
    }

    public ProjectId getProjectId() {
        return projectId;
    }

    public boolean isUnderMaintenance() {
        return underMaintenance;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectId, underMaintenance);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SetProjectUnderMaintenanceAction)) {
            return false;
        }
        SetProjectUnderMaintenanceAction other = (SetProjectUnderMaintenanceAction) obj;
        return this.projectId.equals(other.projectId) && this.underMaintenance == other.underMaintenance;
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper("SetProjectUnderMaintenanceAction")
                          .add("projectId", projectId)
                          .add("underMaintenance", underMaintenance)
                          .toString();
    }
}

