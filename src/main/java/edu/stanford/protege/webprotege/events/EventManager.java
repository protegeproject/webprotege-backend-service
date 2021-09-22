package edu.stanford.protege.webprotege.events;

import edu.stanford.protege.webprotege.HasDispose;
import edu.stanford.protege.webprotege.common.Event;
import edu.stanford.protege.webprotege.inject.ProjectSingleton;
import edu.stanford.protege.webprotege.ipc.EventDispatcher;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static java.util.stream.Collectors.toList;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/03/2013
 */
@ProjectSingleton
public class EventManager<E extends Event> implements HasDispose {

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    private final Lock writeLock = lock.writeLock();

    private final EventDispatcher eventDispatcher;

    @Inject
    public EventManager(EventDispatcher eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
    }

    public void postHighLevelEvents(List<HighLevelProjectEventProxy> eventProxies) {
        var realEvents = eventProxies.stream().map(e -> (E) e.asProjectEvent()).collect(toList());
        postEvents(realEvents);
    }

    /**
     * Posts a list of events to this event manager.
     *
     * @param events The list of events to be posted.  Not {@code null}.
     * @throws NullPointerException if {@code events} is {@code null}.
     */
    private void postEvents(List<E> events) {
        try {
            writeLock.lock();
            events.forEach(eventDispatcher::dispatchEvent);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void dispose() {

    }
}
