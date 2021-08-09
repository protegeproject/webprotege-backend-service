package edu.stanford.protege.webprotege.project;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.dispatch.Action;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/04/2013
 */
@AutoValue

@JsonTypeName("MoveProjectsToTrash")
public abstract class MoveProjectsToTrashAction implements Action<MoveProjectsToTrashResult> {

    @JsonCreator
    public static MoveProjectsToTrashAction create(@JsonProperty("projectId") ProjectId projectId) {
        return new AutoValue_MoveProjectsToTrashAction(projectId);
    }

    public abstract ProjectId getProjectId();
}
