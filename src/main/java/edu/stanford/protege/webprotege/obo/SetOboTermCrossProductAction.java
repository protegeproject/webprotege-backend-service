package edu.stanford.protege.webprotege.obo;

import com.google.common.base.Objects;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.project.ProjectId;
import org.semanticweb.owlapi.model.OWLClass;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Jun 2017
 */
public class SetOboTermCrossProductAction implements ProjectAction<SetOboTermCrossProductResult> {

    private ProjectId projectId;

    private OWLClass entity;

    private OBOTermCrossProduct crossProduct;

    private SetOboTermCrossProductAction(@Nonnull ProjectId projectId,
                                         @Nonnull OWLClass entity,
                                         @Nonnull OBOTermCrossProduct crossProduct) {
        this.projectId = checkNotNull(projectId);
        this.entity = checkNotNull(entity);
        this.crossProduct = checkNotNull(crossProduct);
    }


    private SetOboTermCrossProductAction() {
    }

    public static SetOboTermCrossProductAction create(@Nonnull ProjectId projectId,
                                                      @Nonnull OWLClass entity,
                                                      @Nonnull OBOTermCrossProduct crossProduct) {
        return new SetOboTermCrossProductAction(projectId, entity, crossProduct);
    }

    @Nonnull
    public ProjectId getProjectId() {
        return projectId;
    }

    @Nonnull
    public OWLClass getEntity() {
        return entity;
    }

    @Nonnull
    public OBOTermCrossProduct getCrossProduct() {
        return crossProduct;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectId, entity, crossProduct);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SetOboTermCrossProductAction)) {
            return false;
        }
        SetOboTermCrossProductAction other = (SetOboTermCrossProductAction) obj;
        return this.projectId.equals(other.projectId)
                && this.entity.equals(other.entity)
                && this.crossProduct.equals(other.crossProduct);
    }


    @Override
    public String toString() {
        return toStringHelper("SetOboTermCrossProductAction")
                .addValue(projectId)
                .addValue(entity)
                .addValue(crossProduct)
                .toString();
    }
}
