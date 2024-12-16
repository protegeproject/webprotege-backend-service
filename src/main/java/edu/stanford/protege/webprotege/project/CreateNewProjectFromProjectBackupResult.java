package edu.stanford.protege.webprotege.project;

import com.fasterxml.jackson.annotation.*;
import com.google.common.base.Objects;
import edu.stanford.protege.webprotege.dispatch.Result;

import static com.google.common.base.MoreObjects.toStringHelper;

@JsonTypeName(CreateNewProjectFromProjectBackupAction.CHANNEL)
public record CreateNewProjectFromProjectBackupResult(
        @JsonProperty("projectDetails") ProjectDetails projectDetails
) implements Result {

    public static CreateNewProjectFromProjectBackupResult create(ProjectDetails projectDetails) {
        return new CreateNewProjectFromProjectBackupResult(projectDetails);
    }


    @Override
    public int hashCode() {
        return Objects.hashCode(projectDetails);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CreateNewProjectFromProjectBackupResult)) {
            return false;
        }
        CreateNewProjectFromProjectBackupResult other = (CreateNewProjectFromProjectBackupResult) obj;
        return this.projectDetails.equals(other.projectDetails);
    }


    @Override
    public String toString() {
        return toStringHelper("CreateNewProjectFromProjectBackupResult")
                .addValue(projectDetails)
                .toString();
    }
}
