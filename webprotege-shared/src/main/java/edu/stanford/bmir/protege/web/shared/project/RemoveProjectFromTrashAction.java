package edu.stanford.bmir.protege.web.shared.project;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/04/2013
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("RemoveProjectFromTrash")
public abstract class RemoveProjectFromTrashAction implements Action<RemoveProjectFromTrashResult> {


    @JsonCreator
    public static RemoveProjectFromTrashAction create(@JsonProperty("projectId") ProjectId projectId) {
        return new AutoValue_RemoveProjectFromTrashAction(projectId);
    }

    public abstract ProjectId getProjectId();
}
