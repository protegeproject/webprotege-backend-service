package edu.stanford.protege.webprotege.icd.projects;

import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.dispatch.Result;

import static edu.stanford.protege.webprotege.icd.projects.CreateNewReproducibleProjectRequest.CHANNEL;

@JsonTypeName(CHANNEL)
public record CreateNewReproducibleProjectResponse() implements Result {
}
