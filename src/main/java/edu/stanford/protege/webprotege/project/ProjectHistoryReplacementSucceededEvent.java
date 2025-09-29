package edu.stanford.protege.webprotege.project;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.stanford.protege.webprotege.common.BlobLocation;
import edu.stanford.protege.webprotege.common.EventId;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.jetbrains.annotations.NotNull;

/**
 * Emitted when a project’s revision history has been successfully replaced.
 */
public record ProjectHistoryReplacementSucceededEvent(@JsonProperty("eventId") EventId eventId,
                                                      @JsonProperty("operationId") ProjectHistoryReplacementOperationId operationId,
                                                      @JsonProperty("projectId") ProjectId projectId,
                                                      @JsonProperty("blobLocation") BlobLocation blobLocation) implements ProjectHistoryReplacementCompletionEvent {

    public static final String CHANNEL = "webprotege.events.projects.ProjectHistoryReplacementSucceeded";

    @Override
    public String getChannel() {
        return CHANNEL;
    }

    @NotNull
    @Override
    public ProjectHistoryReplacementOperationId operationId() {
        return operationId;
    }
}
