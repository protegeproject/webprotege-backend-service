package edu.stanford.protege.webprotege.perspective;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.common.ProjectId;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-03
 */
@AutoValue

@JsonTypeName("GetPerspectiveDetails")
public abstract class GetPerspectiveDetailsAction implements ProjectAction<GetPerspectiveDetailsResult> {

    @JsonCreator
    public static GetPerspectiveDetailsAction create(@JsonProperty("projectId") @Nonnull ProjectId projectId) {
        return new AutoValue_GetPerspectiveDetailsAction(projectId);
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();
}
