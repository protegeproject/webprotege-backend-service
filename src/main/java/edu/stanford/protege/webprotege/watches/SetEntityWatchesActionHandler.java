package edu.stanford.protege.webprotege.watches;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.dispatch.ExecutionContext;
import edu.stanford.protege.webprotege.event.EventTag;
import edu.stanford.protege.webprotege.event.ProjectEvent;
import edu.stanford.protege.webprotege.events.EventManager;
import edu.stanford.protege.webprotege.common.UserId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 29/02/16
 */
public class SetEntityWatchesActionHandler extends AbstractProjectActionHandler<SetEntityWatchesAction, SetEntityWatchesResult> {

    private EventManager<ProjectEvent<?>> eventManager;

    private WatchManager watchManager;

    @Inject
    public SetEntityWatchesActionHandler(@Nonnull AccessManager accessManager,
                                         EventManager<ProjectEvent<?>> eventManager,
                                         WatchManager watchManager) {
        super(accessManager);
        this.eventManager = eventManager;
        this.watchManager = watchManager;
    }

    @Nonnull
    @Override
    public Class<SetEntityWatchesAction> getActionClass() {
        return SetEntityWatchesAction.class;
    }

    @Nonnull
    @Override
    public SetEntityWatchesResult execute(@Nonnull SetEntityWatchesAction action, @Nonnull ExecutionContext executionContext) {
        EventTag startTag = eventManager.getCurrentTag();
        UserId userId = action.getUserId();
        Set<Watch> watches = watchManager.getDirectWatches(action.getEntity(), userId);
        for(Watch watch : watches) {
            watchManager.removeWatch(watch);
        }
        for(Watch watch : action.getWatches()) {
            watchManager.addWatch(watch);
        }
        return SetEntityWatchesResult.create(eventManager.getEventsFromTag(startTag));
    }

}
