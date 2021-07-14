package edu.stanford.protege.webprotege.project;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.Result;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 01/04/2013
 */
@AutoValue

@JsonTypeName("GetAvailableProjects")
public abstract class GetAvailableProjectsResult implements Result {

    @JsonCreator
    public static GetAvailableProjectsResult create(@JsonProperty("availableProjects") List<AvailableProject> availableProjects) {
        return new AutoValue_GetAvailableProjectsResult(availableProjects);
    }


    public abstract List<AvailableProject> getAvailableProjects();
}
