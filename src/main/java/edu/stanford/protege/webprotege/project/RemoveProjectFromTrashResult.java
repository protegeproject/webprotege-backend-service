package edu.stanford.protege.webprotege.project;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.event.EventList;
import edu.stanford.protege.webprotege.event.HasEventList;
import edu.stanford.protege.webprotege.event.WebProtegeEvent;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/04/2013
 */
@AutoValue

@JsonTypeName("RemoveProjectFromTrash")
public abstract class RemoveProjectFromTrashResult implements Result, HasEventList<WebProtegeEvent<?>> {

    @JsonCreator
    public static RemoveProjectFromTrashResult create(@JsonProperty("eventList") EventList<WebProtegeEvent<?>> eventList) {
        return new AutoValue_RemoveProjectFromTrashResult(eventList);
    }
}
