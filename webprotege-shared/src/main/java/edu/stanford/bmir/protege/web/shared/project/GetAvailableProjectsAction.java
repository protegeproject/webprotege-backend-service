package edu.stanford.bmir.protege.web.shared.project;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 01/04/2013
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("GetAvailableProjects")
public abstract class GetAvailableProjectsAction implements Action<GetAvailableProjectsResult> {

    @JsonCreator
    public static GetAvailableProjectsAction create() {
        return new AutoValue_GetAvailableProjectsAction();
    }
}
