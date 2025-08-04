package edu.stanford.protege.webprotege.bulkop;

import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.EventId;
import edu.stanford.protege.webprotege.common.ProjectEvent;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.jetbrains.annotations.NotNull;
import org.semanticweb.owlapi.model.IRI;

@JsonTypeName(ParentsChangedEvent.CHANNEL)
public record ParentsChangedEvent(ProjectId projectId, EventId eventId, IRI entityIri) implements ProjectEvent {

    public final static String CHANNEL = "webprotege.events.projects.ui.ParentsChanged";

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