package edu.stanford.bmir.protege.web.shared.dispatch.actions;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/02/2013
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("GetRootOntologyId")
public abstract class GetRootOntologyIdAction extends AbstractHasProjectAction<GetRootOntologyIdResult> {

    @JsonCreator
    public static GetRootOntologyIdAction create(@JsonProperty("projectId") ProjectId projectId) {
        return new AutoValue_GetRootOntologyIdAction(projectId);
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();
}

