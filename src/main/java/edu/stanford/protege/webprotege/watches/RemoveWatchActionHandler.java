package edu.stanford.protege.webprotege.watches;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.dispatch.ExecutionContext;
import edu.stanford.protege.webprotege.event.EventTag;
import edu.stanford.protege.webprotege.event.ProjectEvent;
import edu.stanford.protege.webprotege.events.EventManager;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/03/2013
 */
public class RemoveWatchActionHandler extends AbstractProjectActionHandler<RemoveWatchesAction, RemoveWatchesResult> {

    @Nonnull
    private final EventManager<ProjectEvent<?>> eventManager;

    @Nonnull
    private final WatchManager watchManager;

    @Inject
    public RemoveWatchActionHandler(@Nonnull AccessManager accessManager,
                                    @Nonnull EventManager<ProjectEvent<?>> eventManager,
                                    @Nonnull WatchManager watchManager) {
        super(accessManager);
        this.eventManager = eventManager;
        this.watchManager = watchManager;
    }

    @Nonnull
    @Override
    public RemoveWatchesResult execute(@Nonnull RemoveWatchesAction action, @Nonnull ExecutionContext executionContext) {
        EventTag tag = eventManager.getCurrentTag();
        for(Watch watch : action.getWatches()) {
            watchManager.removeWatch(watch);
        }
        return new RemoveWatchesResult(eventManager.getEventsFromTag(tag));
    }

    @Nonnull
    @Override
    public Class<RemoveWatchesAction> getActionClass() {
        return RemoveWatchesAction.class;
    }
}
