package edu.stanford.bmir.protege.web.server.issues;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;


import edu.stanford.bmir.protege.web.server.dispatch.Result;
import edu.stanford.bmir.protege.web.server.entity.CommentedEntityData;
import edu.stanford.bmir.protege.web.server.pagination.Page;
import edu.stanford.bmir.protege.web.server.project.ProjectId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Mar 2017
 */
@AutoValue

@JsonTypeName("GetCommentedEntities")
public abstract class GetCommentedEntitiesResult implements Result {


    @JsonCreator
    public static GetCommentedEntitiesResult create(@JsonProperty("projectId") ProjectId projectId,
                                                    @JsonProperty("entities") Page<CommentedEntityData> entities) {
        return new AutoValue_GetCommentedEntitiesResult(projectId, entities);
    }

    public abstract ProjectId getProjectId();

    public abstract Page<CommentedEntityData> getEntities();
}
