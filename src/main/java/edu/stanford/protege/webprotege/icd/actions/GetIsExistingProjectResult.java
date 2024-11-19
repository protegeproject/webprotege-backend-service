package edu.stanford.protege.webprotege.icd.actions;

import com.fasterxml.jackson.annotation.*;
import edu.stanford.protege.webprotege.dispatch.Result;

import static edu.stanford.protege.webprotege.icd.actions.GetIsExistingProjectAction.CHANNEL;

@JsonTypeName(CHANNEL)
public record GetIsExistingProjectResult(
        @JsonProperty("isExistingProject") boolean isExistingProject
) implements Result {
    public static GetIsExistingProjectResult create(boolean isExistingProject) {
        return new GetIsExistingProjectResult(isExistingProject);
    }
}
