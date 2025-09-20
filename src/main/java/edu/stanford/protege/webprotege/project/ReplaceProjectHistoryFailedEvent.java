package edu.stanford.protege.webprotege.project;

import edu.stanford.protege.webprotege.common.BlobLocation;
import edu.stanford.protege.webprotege.common.EventId;
import edu.stanford.protege.webprotege.common.ProjectEvent;
import edu.stanford.protege.webprotege.common.ProjectId;

/**
 * Emitted when replacing a project’s revision history fails.
 */
public record ReplaceProjectHistoryFailedEvent(EventId eventId, ProjectId projectId, BlobLocation blobLocation,
                                               String errorMessage) implements ProjectEvent {

    public static final String CHANNEL = "webprotege.events.projects.ReplaceProjectHistoryFailed";

    @Override
    public String getChannel() {
        return CHANNEL;
    }
}
