package edu.stanford.protege.webprotege.obo;

import com.google.common.base.Objects;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Jun 2017
 */
public class SetOboTermIdAction implements ProjectAction<SetOboTermIdResult> {

    private ProjectId projectId;

    private OWLEntity entity;

    private OBOTermId oboTermId;

    private SetOboTermIdAction(@Nonnull ProjectId projectId, @Nonnull OWLEntity entity, @Nonnull OBOTermId oboTermId) {
        this.projectId = checkNotNull(projectId);
        this.entity = checkNotNull(entity);
        this.oboTermId = checkNotNull(oboTermId);
    }


    private SetOboTermIdAction() {
    }

    public static SetOboTermIdAction create(@Nonnull ProjectId projectId,
                                            @Nonnull OWLEntity entity,
                                            @Nonnull OBOTermId oboTermId) {
        return new SetOboTermIdAction(projectId, entity, oboTermId);
    }

    @Nonnull
    public ProjectId getProjectId() {
        return projectId;
    }

    @Nonnull
    public OWLEntity getEntity() {
        return entity;
    }

    @Nonnull
    public OBOTermId getOboTermId() {
        return oboTermId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectId, entity, oboTermId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SetOboTermIdAction)) {
            return false;
        }
        SetOboTermIdAction other = (SetOboTermIdAction) obj;
        return this.entity.equals(other.entity)
                && this.projectId.equals(other.projectId)
                && this.oboTermId.equals(other.oboTermId);
    }


    @Override
    public String toString() {
        return toStringHelper("SetOboTermIdAction")
                .addValue(projectId)
                .addValue(entity)
                .addValue(oboTermId)
                .toString();
    }
}
