package edu.stanford.protege.webprotege.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/03/2013
 * <p>
 *     Represents a list of {@link WebProtegeEvent}s between to points denoted by {@link EventTag}s.
 * </p>
 */
@AutoValue

public abstract class EventList<E extends WebProtegeEvent<?>> implements Serializable {


    @JsonCreator
    public static <E extends WebProtegeEvent<?>> EventList<E> create(@JsonProperty("startTag") EventTag startTag,
                                                        @JsonProperty("events") ImmutableList<E> events,
                                                        @JsonProperty("endTag") EventTag endTag) {
        return new AutoValue_EventList<>(startTag, events, endTag);
    }

    public static <E extends WebProtegeEvent<?>> EventList<E> create(EventTag startTag,
                                                        EventTag endTag) {
        return create(startTag, ImmutableList.of(), endTag);
    }

    @JsonIgnore
    public int size() {
        return getEvents().size();
    }

    @JsonIgnore
    public boolean isEmpty() {
        return getEvents().size() == 0;
    }

    public abstract EventTag getStartTag();

    public abstract ImmutableList<E> getEvents();

    public abstract EventTag getEndTag();
}
