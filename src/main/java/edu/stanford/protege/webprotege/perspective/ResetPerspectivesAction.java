package edu.stanford.protege.webprotege.perspective;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.project.ProjectId;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-03
 */
@AutoValue

@JsonTypeName("ResetPerspectives")
public abstract class ResetPerspectivesAction implements ProjectAction<ResetPerspectivesResult> {


    @JsonCreator
    public static ResetPerspectivesAction create(@JsonProperty("projectId") @Nonnull ProjectId projectId) {
        return new AutoValue_ResetPerspectivesAction(projectId);
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();
}
