package edu.stanford.protege.webprotege.watches;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 29/02/16
 */
public class SetWatchesActionHandler extends AbstractProjectActionHandler<SetWatchesAction, SetWatchesResult> {

    private final WatchManager watchManager;

    @Inject
    public SetWatchesActionHandler(@Nonnull AccessManager accessManager,
                                   WatchManager watchManager) {
        super(accessManager);
        this.watchManager = watchManager;
    }

    @Nonnull
    @Override
    public Class<SetWatchesAction> getActionClass() {
        return SetWatchesAction.class;
    }

    @Nonnull
    @Override
    public SetWatchesResult execute(@Nonnull SetWatchesAction action, @Nonnull ExecutionContext executionContext) {
        UserId userId = action.userId();
        Set<Watch> watches = watchManager.getDirectWatches(action.entity(), userId);
        for(Watch watch : watches) {
            watchManager.removeWatch(watch);
        }
        for(Watch watch : action.watches()) {
            watchManager.addWatch(watch);
        }
        return new SetWatchesResult();
    }

}
