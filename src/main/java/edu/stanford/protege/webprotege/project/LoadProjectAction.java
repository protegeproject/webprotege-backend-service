package edu.stanford.protege.webprotege.project;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.Objects;
import edu.stanford.protege.webprotege.dispatch.Action;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/04/2013
 */
@JsonTypeName("LoadProject")
public class LoadProjectAction implements Action<LoadProjectResult>, HasProjectId {

    private ProjectId projectId;

    /**
     * For serialization purposes onlu
     */
    private LoadProjectAction() {

    }

    @JsonCreator
    public LoadProjectAction(@JsonProperty("projectId") ProjectId projectId) {
        this.projectId = checkNotNull(projectId);
    }

    public static LoadProjectAction create(ProjectId projectId) {
        return new LoadProjectAction(projectId);
    }

    @Nonnull
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
        if (!(obj instanceof LoadProjectAction)) {
            return false;
        }
        LoadProjectAction other = (LoadProjectAction) obj;
        return this.projectId.equals(other.projectId);
    }


    @Override
    public String toString() {
        return toStringHelper("LoadProjectAction")
                .addValue(projectId)
                .toString();
    }
}
