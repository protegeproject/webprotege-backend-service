package edu.stanford.protege.webprotege.entity;

import com.google.common.base.Objects;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.project.HasProjectId;
import edu.stanford.protege.webprotege.project.ProjectId;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10 May 2017
 */
public class ChangeEntityIRIResult implements Result, HasProjectId {

    private ProjectId projectId;

    private OWLEntityData theOldEntity;

    private OWLEntityData theNewEntity;


    private ChangeEntityIRIResult() {
    }

    public ChangeEntityIRIResult(@Nonnull ProjectId projectId,
                                 @Nonnull OWLEntityData theOldEntity,
                                 @Nonnull OWLEntityData theNewEntity) {
        this.projectId = checkNotNull(projectId);
        this.theOldEntity = checkNotNull(theOldEntity);
        this.theNewEntity = checkNotNull(theNewEntity);
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    public OWLEntityData getTheOldEntity() {
        return theOldEntity;
    }

    public OWLEntityData getTheNewEntity() {
        return theNewEntity;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectId, theOldEntity, theNewEntity);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ChangeEntityIRIResult)) {
            return false;
        }
        ChangeEntityIRIResult other = (ChangeEntityIRIResult) obj;
        return this.projectId.equals(other.projectId)
                && this.theOldEntity.equals(other.theOldEntity)
                && this.theNewEntity.equals(other.theNewEntity);
    }


    @Override
    public String toString() {
        return toStringHelper("ChangeEntityIRIResult")
                .addValue(projectId)
                .add("old", theOldEntity)
                .add("new", theNewEntity)
                .toString();
    }
}
