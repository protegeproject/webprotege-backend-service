package edu.stanford.protege.webprotege.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.Objects;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 9 May 2017
 */
@JsonTypeName("DeleteEntities")
public class DeleteEntitiesAction implements ProjectAction<DeleteEntitiesResult> {

    private ProjectId projectId;

    private Set<OWLEntity> entities;


    private DeleteEntitiesAction() {
    }

    @JsonCreator
    public DeleteEntitiesAction(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                @JsonProperty("entities") @Nonnull Set<OWLEntity> entities) {
        this.entities = new HashSet<>(entities);
        this.projectId = checkNotNull(projectId);
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    public Set<OWLEntity> getEntities() {
        return new HashSet<>(entities);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectId, entities);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof DeleteEntitiesAction)) {
            return false;
        }
        DeleteEntitiesAction other = (DeleteEntitiesAction) obj;
        return this.projectId.equals(other.projectId)
                && this.entities.equals(other.entities);
    }


    @Override
    public String toString() {
        return toStringHelper("DeleteEntitiesAction")
                .addValue(projectId)
                .addValue(entities)
                .toString();
    }
}
