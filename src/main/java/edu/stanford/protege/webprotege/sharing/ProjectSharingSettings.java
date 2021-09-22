package edu.stanford.protege.webprotege.sharing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import edu.stanford.protege.webprotege.common.ProjectId;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 27/02/2012
 */
public class ProjectSharingSettings implements Serializable {

    private final ProjectId projectId;
    
    private final List<SharingSetting> sharingSettings = new ArrayList<>();

    @Nullable
    private SharingPermission linkSharingPermission = null;

    @JsonCreator
    public ProjectSharingSettings(@JsonProperty("projectId") ProjectId projectId,
                                  @JsonProperty("linkSharingPermission") Optional<SharingPermission> linkSharingPermission,
                                  @JsonProperty("sharingSettings") List<SharingSetting> sharingSettings) {
        this.projectId = checkNotNull(projectId);
        this.sharingSettings.addAll(checkNotNull(sharingSettings));
        this.linkSharingPermission = checkNotNull(linkSharingPermission).orElse(null);
    }

    public ProjectId getProjectId() {
        return projectId;
    }

    public List<SharingSetting> getSharingSettings() {
        return new ArrayList<>(sharingSettings);
    }

    public Optional<SharingPermission> getLinkSharingPermission() {
        return Optional.ofNullable(linkSharingPermission);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectId, linkSharingPermission, sharingSettings);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof ProjectSharingSettings)) {
            return false;
        }
        ProjectSharingSettings other = (ProjectSharingSettings) obj;
        return other.projectId.equals(this.projectId) && Objects.equal(this.linkSharingPermission, other.linkSharingPermission) && other.sharingSettings.equals(this.sharingSettings);
    }


    @Override
    public String toString() {
        return toStringHelper("ProjectSharingSettings")
                .addValue(projectId)
                .addValue(sharingSettings)
                .toString();
    }
}
