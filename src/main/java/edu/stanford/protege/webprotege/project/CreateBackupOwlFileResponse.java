package edu.stanford.protege.webprotege.project;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.Response;
import edu.stanford.protege.webprotege.dispatch.Result;

import static edu.stanford.protege.webprotege.project.CreateBackupOwlFileAction.CHANNEL;

@JsonTypeName(CHANNEL)
public record CreateBackupOwlFileResponse(@JsonProperty("owlFileBackupLocation") String owlFileBackupLocation) implements Result, Response {
}
