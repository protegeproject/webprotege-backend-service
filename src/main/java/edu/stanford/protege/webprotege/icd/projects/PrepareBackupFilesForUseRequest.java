package edu.stanford.protege.webprotege.icd.projects;

import com.fasterxml.jackson.annotation.*;
import edu.stanford.protege.webprotege.common.*;
import edu.stanford.protege.webprotege.csv.DocumentId;

import static edu.stanford.protege.webprotege.icd.projects.PrepareBackupFilesForUseRequest.CHANNEL;

@JsonTypeName(CHANNEL)
public record PrepareBackupFilesForUseRequest(
        @JsonProperty("projectId") ProjectId projectId,
        @JsonProperty("fileSubmissionId") DocumentId fileSubmissionId
) implements Request<PrepareBackupFilesForUseResponse> {

    public static final String CHANNEL = "icatx.versioning.PrepareBinaryFileBackupForUse";

    @Override
    public String getChannel() {
        return CHANNEL;
    }
}
