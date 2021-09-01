package edu.stanford.protege.webprotege.event;

import edu.stanford.protege.webprotege.common.Event;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/03/2013
 */
public interface HasEventList<T extends Event> {

    EventList<T> getEventList();

}
