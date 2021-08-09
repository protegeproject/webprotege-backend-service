package edu.stanford.protege.webprotege.issues;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 6 Oct 2016
 */
@AutoValue

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
