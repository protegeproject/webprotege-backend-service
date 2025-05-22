package edu.stanford.protege.webprotege.icd.projects;

import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.dispatch.Result;

import java.util.List;

import static edu.stanford.protege.webprotege.icd.projects.GetReproducibleProjectsRequest.CHANNEL;

@JsonTypeName(CHANNEL)
public record GetReproducibleProjectsResponse(List<ReproducibleProject> reproducibleProjectList) implements Result {
}
