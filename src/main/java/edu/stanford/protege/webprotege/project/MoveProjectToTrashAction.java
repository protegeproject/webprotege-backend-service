package edu.stanford.protege.webprotege.project;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.Request;
import edu.stanford.protege.webprotege.dispatch.Action;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/04/2013
 */
@AutoValue

@JsonTypeName("MoveProjectToTrash")
public abstract class MoveProjectToTrashAction implements Action<MoveProjectToTrashResult>, Request<MoveProjectToTrashResult> {

    public static final String CHANNEL = "project-management.MoveProjectToTrash";

    @JsonCreator
    public static MoveProjectToTrashAction create(@JsonProperty("projectId") ProjectId projectId) {
        return new AutoValue_MoveProjectToTrashAction(projectId);
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }

    public abstract ProjectId getProjectId();
}
