package edu.stanford.protege.webprotege.change;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.Objects;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.revision.RevisionNumber;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19/03/15
 */
@JsonTypeName("RevertRevision")
public class RevertRevisionAction implements ProjectAction<RevertRevisionResult> {

    private RevisionNumber revisionNumber;

    private ProjectId projectId;

    /**
     * For serialization
     */
    private RevertRevisionAction() {
    }

    private RevertRevisionAction(ProjectId projectId, RevisionNumber revisionNumber) {
        this.projectId = checkNotNull(projectId);
        this.revisionNumber = checkNotNull(revisionNumber);
    }

    @JsonCreator
    public static RevertRevisionAction create(@JsonProperty("projectId") ProjectId projectId,
                                              @JsonProperty("revisionNumber") RevisionNumber revisionNumber) {
        return new RevertRevisionAction(projectId, revisionNumber);
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    public RevisionNumber getRevisionNumber() {
        return revisionNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectId, revisionNumber);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof RevertRevisionAction)) {
            return false;
        }
        RevertRevisionAction other = (RevertRevisionAction) obj;
        return this.projectId.equals(other.projectId) && this.revisionNumber.equals(other.revisionNumber);
    }


    @Override
    public String toString() {
        return toStringHelper("RevertRevisionAction")
                .addValue(projectId)
                .addValue(revisionNumber)
                .toString();
    }
}
