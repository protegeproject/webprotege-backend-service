package edu.stanford.protege.webprotege.issues;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.entity.CommentedEntityData;
import edu.stanford.protege.webprotege.pagination.Page;
import edu.stanford.protege.webprotege.common.ProjectId;

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
