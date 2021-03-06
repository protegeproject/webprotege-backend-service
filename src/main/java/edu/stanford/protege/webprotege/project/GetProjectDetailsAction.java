package edu.stanford.protege.webprotege.project;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.Objects;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.dispatch.Action;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10/03/16
 */
@JsonTypeName("webprotege.projects.GetProjectDetails")
public class GetProjectDetailsAction implements Action<GetProjectDetailsResult>, HasProjectId {

    public static final String CHANNEL = "webprotege.projects.GetProjectDetails";

    private final ProjectId projectId;

    @JsonCreator
    public GetProjectDetailsAction(@JsonProperty("projectId") ProjectId projectId) {
        this.projectId = checkNotNull(projectId);
    }

    public static GetProjectDetailsAction create(ProjectId projectId) {
        return new GetProjectDetailsAction(projectId);
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
    public int hashCode() {
        return Objects.hashCode(projectId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetProjectDetailsAction)) {
            return false;
        }
        GetProjectDetailsAction other = (GetProjectDetailsAction) obj;
        return this.projectId.equals(other.projectId);
    }


    @Override
    public String toString() {
        return toStringHelper("GetProjectDetailsAction")
                .addValue(projectId)
                .toString();
    }
}
