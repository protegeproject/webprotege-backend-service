package edu.stanford.protege.webprotege.project;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import edu.stanford.protege.webprotege.dispatch.Result;

/**
 * Result of setting the under maintenance status of a project.
 */
public class SetProjectUnderMaintenanceResult implements Result {

    private boolean underMaintenance;

    private SetProjectUnderMaintenanceResult() {

    }

    private SetProjectUnderMaintenanceResult(boolean underMaintenance) {
        this.underMaintenance = underMaintenance;
    }

    public static SetProjectUnderMaintenanceResult create(boolean underMaintenance) {
        return new SetProjectUnderMaintenanceResult(underMaintenance);
    }

    public boolean isUnderMaintenance() {
        return underMaintenance;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(underMaintenance);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SetProjectUnderMaintenanceResult)) {
            return false;
        }
        SetProjectUnderMaintenanceResult other = (SetProjectUnderMaintenanceResult) obj;
        return this.isUnderMaintenance() == other.isUnderMaintenance();
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper("SetProjectUnderMaintenanceResult")
                          .addValue(underMaintenance)
                          .toString();
    }
}

