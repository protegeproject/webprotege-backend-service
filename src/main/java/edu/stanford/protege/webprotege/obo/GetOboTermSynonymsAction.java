package edu.stanford.protege.webprotege.obo;

import com.google.common.base.Objects;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Jun 2017
 */
public class GetOboTermSynonymsAction implements ProjectAction<GetOboTermSynonymsResult> {

    private ProjectId projectId;

    private OWLEntity entity;

    private GetOboTermSynonymsAction(@Nonnull ProjectId projectId, @Nonnull OWLEntity entity) {

        this.projectId = checkNotNull(projectId);
        this.entity = checkNotNull(entity);
    }


    private GetOboTermSynonymsAction() {
    }

    public static GetOboTermSynonymsAction create(@Nonnull ProjectId projectId, @Nonnull OWLEntity entity) {
        return new GetOboTermSynonymsAction(projectId, entity);
    }

    @Override
    @Nonnull
    public ProjectId getProjectId() {
        return projectId;
    }

    @Nonnull
    public OWLEntity getEntity() {
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
        if (!(obj instanceof GetOboTermSynonymsAction)) {
            return false;
        }
        GetOboTermSynonymsAction other = (GetOboTermSynonymsAction) obj;
        return this.projectId.equals(other.projectId)
                && this.entity.equals(other.entity);
    }


    @Override
    public String toString() {
        return toStringHelper("GetOboTermSynonymsAction")
                .addValue(projectId)
                .addValue(entity)
                .toString();
    }
}
