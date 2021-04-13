package edu.stanford.bmir.protege.web.shared.project;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 01/04/2013
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("GetAvailableProjects")
public abstract class GetAvailableProjectsResult implements Result {

    @JsonCreator
    public static GetAvailableProjectsResult create(@JsonProperty("availableProjects") List<AvailableProject> availableProjects) {
        return new AutoValue_GetAvailableProjectsResult(availableProjects);
    }


    public abstract List<AvailableProject> getAvailableProjects();
}
