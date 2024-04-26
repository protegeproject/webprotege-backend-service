package edu.stanford.protege.webprotege.project;

import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.EventId;
import edu.stanford.protege.webprotege.common.ProjectEvent;
import edu.stanford.protege.webprotege.common.ProjectId;

import javax.annotation.Nonnull;
import java.util.List;

@JsonTypeName(PackagedProjectChangeEvent.CHANNEL)
public record PackagedProjectChangeEvent(ProjectId projectId, EventId eventId, List<ProjectEvent> projectEvents) implements ProjectEvent {

    public final static String CHANNEL = "webprotege.events.projects.PackagedProjectChange";



    @Nonnull
    @Override
    public ProjectId projectId() {
        return projectId;
    }

    @Nonnull
    @Override
    public EventId eventId() {
        return eventId;
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }


}
