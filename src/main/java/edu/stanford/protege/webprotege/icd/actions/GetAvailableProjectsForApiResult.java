package edu.stanford.protege.webprotege.icd.actions;

import com.fasterxml.jackson.annotation.*;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.icd.dtos.ProjectSummaryDto;

import java.util.List;

import static edu.stanford.protege.webprotege.icd.actions.GetAvailableProjectsForApiAction.CHANNEL;

@JsonTypeName(CHANNEL)
public record GetAvailableProjectsForApiResult(
        @JsonProperty("availableProjects") List<ProjectSummaryDto> availableProjects
) implements Result {
}
