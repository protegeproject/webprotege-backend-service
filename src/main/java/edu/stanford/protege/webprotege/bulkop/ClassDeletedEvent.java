package edu.stanford.protege.webprotege.bulkop;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.EventId;
import edu.stanford.protege.webprotege.common.ProjectEvent;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.jetbrains.annotations.NotNull;
import org.semanticweb.owlapi.model.IRI;

import java.util.List;

@JsonTypeName(ClassDeletedEvent.CHANNEL)
public record ClassDeletedEvent(@JsonProperty("projectId") ProjectId projectId, @JsonProperty("eventId") EventId eventId , @JsonProperty("deletedIris") List<IRI> deletedIris) implements ProjectEvent {

    public final static String CHANNEL = "webprotege.events.projects.ClassDeleted";


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

    @NotNull
    public List<IRI> deletedIris() {
        return deletedIris;
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }
}
