package edu.stanford.protege.webprotege.icd.projects;

import com.fasterxml.jackson.annotation.*;
import edu.stanford.protege.webprotege.common.BlobLocation;
import edu.stanford.protege.webprotege.dispatch.Result;

import static edu.stanford.protege.webprotege.icd.projects.PrepareBackupFilesForUseRequest.CHANNEL;

@JsonTypeName(CHANNEL)
public record PrepareBackupFilesForUseResponse(
        @JsonProperty("binaryFileLocation") BlobLocation binaryFileLocation
) implements Result {
}
