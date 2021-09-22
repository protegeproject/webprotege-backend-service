package edu.stanford.protege.webprotege.watches;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.dispatch.ExecutionContext;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 29/02/16
 */
public class SetEntityWatchesActionHandler extends AbstractProjectActionHandler<SetEntityWatchesAction, SetEntityWatchesResult> {

    private final WatchManager watchManager;

    @Inject
    public SetEntityWatchesActionHandler(@Nonnull AccessManager accessManager,
                                         WatchManager watchManager) {
        super(accessManager);
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
        UserId userId = action.userId();
        Set<Watch> watches = watchManager.getDirectWatches(action.entity(), userId);
        for(Watch watch : watches) {
            watchManager.removeWatch(watch);
        }
        for(Watch watch : action.watches()) {
            watchManager.addWatch(watch);
        }
        return new SetEntityWatchesResult();
    }

}
