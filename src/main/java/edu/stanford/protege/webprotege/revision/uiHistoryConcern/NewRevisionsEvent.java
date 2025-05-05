package edu.stanford.protege.webprotege.revision.uiHistoryConcern;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.common.*;
import org.jetbrains.annotations.NotNull;


@JsonTypeName(NewRevisionsEvent.CHANNEL)
public record NewRevisionsEvent(
        EventId eventId,
        ProjectId projectId,
        ImmutableList<ProjectChangeForEntity> changes,
        ChangeRequestId changeRequestId
) implements ProjectEvent {
    public final static String CHANNEL = "webprotege.events.projects.uiHistory.NewRevisionsEvent";

    public static NewRevisionsEvent create(EventId eventId,
                                           ProjectId projectId,
                                           ImmutableList<ProjectChangeForEntity> changes,
                                           ChangeRequestId changeRequestId) {
        return new NewRevisionsEvent(eventId, projectId, changes, changeRequestId);
    }

    @NotNull
    @Override
    public ProjectId projectId() {
        return projectId;
    }

    @NotNull
    @Override
    public EventId eventId() {
        return eventId;
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }
}
