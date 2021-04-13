package edu.stanford.bmir.protege.web.shared.issues;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 6 Oct 2016
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("CreateEntityDiscussionThread")
public abstract class CreateEntityDiscussionThreadAction implements ProjectAction<CreateEntityDiscussionThreadResult> {

    @Nonnull
    public abstract ProjectId getProjectId();

    public abstract OWLEntity getEntity();

    @Nonnull
    public abstract String getComment();

    @JsonCreator
    public static CreateEntityDiscussionThreadAction create(@JsonProperty("projectId") ProjectId projectId,
                                                            @JsonProperty("entity") OWLEntity entity,
                                                            @JsonProperty("comment") String comment) {
        return new AutoValue_CreateEntityDiscussionThreadAction(projectId, entity, comment);
    }


}
