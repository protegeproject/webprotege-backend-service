package edu.stanford.bmir.protege.web.shared.perspective;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18/02/16
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("GetPerspectives")
public abstract class GetPerspectivesAction implements Action<GetPerspectivesResult> {

    @JsonCreator
    public static GetPerspectivesAction create(@JsonProperty("projectId") ProjectId projectId,
                                               @JsonProperty("userId") UserId userId) {
        return new AutoValue_GetPerspectivesAction(projectId, userId);
    }

    @Nonnull
    public abstract ProjectId getProjectId();

    public abstract UserId getUserId();
}
