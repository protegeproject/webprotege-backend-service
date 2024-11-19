package edu.stanford.protege.webprotege.icd.actions;

import com.fasterxml.jackson.annotation.*;
import edu.stanford.protege.webprotege.common.*;

@JsonTypeName(GetIsExistingProjectAction.CHANNEL)
public record GetIsExistingProjectAction(
        @JsonProperty("projectId") ProjectId projectId
) implements ProjectRequest<GetIsExistingProjectResult> {
    public static final String CHANNEL = "webprotege.projects.GetIsExistingProject";

    public static GetIsExistingProjectAction create(ProjectId projectId) {
        return new GetIsExistingProjectAction(projectId);
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }
}
