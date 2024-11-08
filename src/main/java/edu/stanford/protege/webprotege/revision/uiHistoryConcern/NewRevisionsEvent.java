package edu.stanford.protege.webprotege.revision.uiHistoryConcern;

import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.*;
import org.jetbrains.annotations.NotNull;

import java.util.Set;


@JsonTypeName(NewRevisionsEvent.CHANNEL)
public record NewRevisionsEvent(
        EventId eventId,
        ProjectId projectId,
        Set<ProjectChangeForEntity> changes
) implements ProjectEvent {
    public final static String CHANNEL = "webprotege.events.projects.uiHistory.NewRevisionsEvent";

    public static NewRevisionsEvent create(EventId eventId,
                                           ProjectId projectId,
                                           Set<ProjectChangeForEntity> changes) {
        return new NewRevisionsEvent(eventId, projectId, changes);
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
