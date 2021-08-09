package edu.stanford.protege.webprotege.obo;

import com.google.common.base.Objects;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.semanticweb.owlapi.model.OWLClass;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Jun 2017
 */
public class GetOboTermCrossProductAction implements ProjectAction<GetOboTermCrossProductResult> {

    private ProjectId projectId;

    private OWLClass entity;

    private GetOboTermCrossProductAction(@Nonnull ProjectId projectId, @Nonnull OWLClass entity) {
        this.projectId = checkNotNull(projectId);
        this.entity = checkNotNull(entity);
    }


    private GetOboTermCrossProductAction() {
    }

    public static GetOboTermCrossProductAction create(@Nonnull ProjectId projectId, @Nonnull OWLClass entity) {
        return new GetOboTermCrossProductAction(projectId, entity);
    }

    @Override
    @Nonnull
    public ProjectId getProjectId() {
        return projectId;
    }

    @Nonnull
    public OWLClass getEntity() {
        return entity;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectId, entity);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetOboTermCrossProductAction)) {
            return false;
        }
        GetOboTermCrossProductAction other = (GetOboTermCrossProductAction) obj;
        return this.projectId.equals(other.projectId)
                && this.entity.equals(other.entity);
    }


    @Override
    public String toString() {
        return toStringHelper("GetOboTermCrossProductAction")
                .addValue(projectId)
                .addValue(entity)
                .toString();
    }
}
