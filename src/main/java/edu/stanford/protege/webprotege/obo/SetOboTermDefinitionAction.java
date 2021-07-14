package edu.stanford.protege.webprotege.obo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Jun 2017
 */
public class SetOboTermDefinitionAction implements ProjectAction<SetOboTermDefinitionResult> {

    public static final String TERM = "term";

    private ProjectId projectId;

    private OWLEntity entity;

    private OBOTermDefinition def;

    private SetOboTermDefinitionAction(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                       @JsonProperty(TERM) @Nonnull OWLEntity entity,
                                       @JsonProperty("definition") @Nonnull OBOTermDefinition def) {
        this.projectId = projectId;
        this.entity = entity;
        this.def = def;
    }


    private SetOboTermDefinitionAction() {
    }

    public static SetOboTermDefinitionAction create(@Nonnull ProjectId projectId,
                                                    @Nonnull OWLEntity entity,
                                                    @Nonnull OBOTermDefinition def) {
        return new SetOboTermDefinitionAction(projectId, entity, def);
    }

    @Nonnull
    public ProjectId getProjectId() {
        return projectId;
    }

    @JsonProperty(TERM)
    @Nonnull
    public OWLEntity getEntity() {
        return entity;
    }

    @Nonnull
    public OBOTermDefinition getDefinition() {
        return def;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectId, entity, def);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SetOboTermDefinitionAction)) {
            return false;
        }
        SetOboTermDefinitionAction other = (SetOboTermDefinitionAction) obj;
        return this.projectId.equals(other.projectId)
                && this.entity.equals(other.entity)
                && this.def.equals(other.def);
    }


    @Override
    public String toString() {
        return toStringHelper("SetOboTermDefinitionAction")
                .addValue(projectId)
                .addValue(entity)
                .addValue(def)
                .toString();
    }
}
