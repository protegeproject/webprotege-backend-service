package edu.stanford.bmir.protege.web.shared.issues;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.entity.CommentedEntityData;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Mar 2017
 */
@AutoValue
@GwtCompatible(serializable = true)
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
