package edu.stanford.protege.webprotege.project;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.common.Response;
import edu.stanford.protege.webprotege.dispatch.Result;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/04/2013
 */
@AutoValue

@JsonTypeName("RemoveProjectFromTrash")
public abstract class RemoveProjectFromTrashResult implements Result, Response {

    @JsonCreator
    public static RemoveProjectFromTrashResult create() {
        return new AutoValue_RemoveProjectFromTrashResult();
    }
}
