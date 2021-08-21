package edu.stanford.protege.webprotege.revision;

import com.google.common.base.Objects;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.common.ProjectId;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/02/15
 */
public class GetHeadRevisionNumberAction implements ProjectAction<GetHeadRevisionNumberResult> {

    public static final String CHANNEL = "webprotege.history.GetHeadRevisionNumber";

    private final ProjectId projectId;

    private GetHeadRevisionNumberAction(ProjectId projectId) {
        this.projectId = checkNotNull(projectId);
    }

    public static GetHeadRevisionNumberAction create(ProjectId projectId) {
        return new GetHeadRevisionNumberAction(projectId);
    }

    @Override
    public String getChannel() {
        return CHANNEL;
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
        if (!(obj instanceof GetHeadRevisionNumberAction)) {
            return false;
        }
        GetHeadRevisionNumberAction other = (GetHeadRevisionNumberAction) obj;
        return this.projectId.equals(other.projectId);
    }


    @Override
    public String toString() {
        return toStringHelper("GetHeadRevisionNumberAction")
                .addValue(projectId)
                .toString();
    }
}
