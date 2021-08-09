package edu.stanford.protege.webprotege.bulkop;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25 Sep 2018
 */
@AutoValue

@JsonTypeName("MoveEntitiesToParent")
public abstract class MoveEntitiesToParentAction implements ProjectAction<MoveEntitiesToParentResult>, HasCommitMessage {

    @JsonCreator
    public static MoveEntitiesToParentAction create(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                                    @JsonProperty("entities") @Nonnull ImmutableSet<OWLClass> entities,
                                                    @JsonProperty("parentEntity") @Nonnull OWLClass entity,
                                                    @JsonProperty("commitMessage") @Nonnull String commitMessage) {
        return new AutoValue_MoveEntitiesToParentAction(projectId, entities, entity, commitMessage);
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    @Nonnull
    public abstract ImmutableSet<? extends OWLEntity> getEntities();

    @Nonnull
    public abstract OWLEntity getParentEntity();

    @Nonnull
    @Override
    public abstract String getCommitMessage();
}
