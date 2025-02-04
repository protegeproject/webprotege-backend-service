package edu.stanford.protege.webprotege.project;

import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.EventId;
import edu.stanford.protege.webprotege.common.ProjectEvent;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.jetbrains.annotations.NotNull;

@JsonTypeName(NewProjectEvent.CHANNEL)
public record NewProjectEvent(ProjectId projectId, EventId eventId) implements ProjectEvent {

    public final static String CHANNEL = "webprotege.events.projects.NewProjectEvent";

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
