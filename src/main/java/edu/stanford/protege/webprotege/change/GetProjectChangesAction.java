package edu.stanford.protege.webprotege.change;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.pagination.PageRequest;
import edu.stanford.protege.webprotege.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24/02/15
 */
public class GetProjectChangesAction implements ProjectAction<GetProjectChangesResult> {

    private ProjectId projectId;

    @Nullable
    private OWLEntity subject;

    private PageRequest pageRequest;


    private GetProjectChangesAction() {
    }

    private GetProjectChangesAction(@Nonnull ProjectId projectId,
                                    @Nonnull Optional<OWLEntity> subject,
                                    @Nonnull PageRequest pageRequest) {
        this.projectId = checkNotNull(projectId);
        this.subject = checkNotNull(subject).orElse(null);
        this.pageRequest = checkNotNull(pageRequest);
    }

    public static GetProjectChangesAction create(@Nonnull ProjectId projectId,
                                                 @Nonnull Optional<OWLEntity> subject,
                                                 @Nonnull PageRequest pageRequest) {
        return new GetProjectChangesAction(projectId, subject, pageRequest);
    }

    @Nonnull
    public ProjectId getProjectId() {
        return projectId;
    }

    @Nonnull
    public Optional<OWLEntity> getSubject() {
        return Optional.ofNullable(subject);
    }

    @Nonnull
    public PageRequest getPageRequest() {
        return pageRequest;
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
        if (!(obj instanceof GetProjectChangesAction)) {
            return false;
        }
        GetProjectChangesAction other = (GetProjectChangesAction) obj;
        return this.projectId.equals(other.projectId)
                && java.util.Objects.equals(this.subject, other.subject);
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper("GetProjectChangesAction")
                .addValue(projectId)
                .addValue(subject)
                .toString();
    }
}
