package edu.stanford.protege.webprotege.project;

import edu.stanford.protege.webprotege.common.BlobLocation;
import edu.stanford.protege.webprotege.common.EventId;
import edu.stanford.protege.webprotege.common.ProjectEvent;
import edu.stanford.protege.webprotege.common.ProjectId;

/**
 * Emitted when a project’s revision history has been successfully replaced.
 */
public record ReplaceProjectHistorySucceededEvent(EventId eventId, ProjectId projectId,
                                                  BlobLocation blobLocation) implements ProjectEvent {

    public static final String CHANNEL = "webprotege.events.projects.ReplaceProjectHistorySucceeded";

    @Override
    public String getChannel() {
        return CHANNEL;
    }
}
