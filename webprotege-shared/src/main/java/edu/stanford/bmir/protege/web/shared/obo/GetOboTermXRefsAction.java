package edu.stanford.bmir.protege.web.shared.obo;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Jun 2017
 */
public class GetOboTermXRefsAction implements ProjectAction<GetOboTermXRefsResult> {

    private ProjectId projectId;

    private OWLEntity entity;

    private GetOboTermXRefsAction(@Nonnull ProjectId projectId, @Nonnull OWLEntity entity) {
        this.projectId = projectId;
        this.entity = entity;
    }

    @GwtSerializationConstructor
    private GetOboTermXRefsAction() {
    }

    public static GetOboTermXRefsAction create(@Nonnull ProjectId projectId, @Nonnull OWLEntity entity) {
        return new GetOboTermXRefsAction(projectId, entity);
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
        if (!(obj instanceof GetOboTermXRefsAction)) {
            return false;
        }
        GetOboTermXRefsAction other = (GetOboTermXRefsAction) obj;
        return this.projectId.equals(other.projectId);
    }


    @Override
    public String toString() {
        return toStringHelper("GetOboTermXRefsAction")
                .addValue(projectId)
                .addValue(entity)
                .toString();
    }
}
