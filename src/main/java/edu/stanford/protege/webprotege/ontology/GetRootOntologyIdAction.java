package edu.stanford.protege.webprotege.ontology;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.project.ProjectId;

import javax.annotation.Nonnull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/02/2013
 */
@AutoValue

@JsonTypeName("GetRootOntologyId")
public abstract class GetRootOntologyIdAction implements ProjectAction<GetRootOntologyIdResult> {

    @JsonCreator
    public static GetRootOntologyIdAction create(@JsonProperty("projectId") ProjectId projectId) {
        return new AutoValue_GetRootOntologyIdAction(projectId);
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();
}

