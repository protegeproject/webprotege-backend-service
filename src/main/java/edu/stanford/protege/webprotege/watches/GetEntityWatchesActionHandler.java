package edu.stanford.protege.webprotege.watches;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.dispatch.ExecutionContext;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.Set;

import static edu.stanford.protege.webprotege.access.BuiltInAction.VIEW_PROJECT;
import static edu.stanford.protege.webprotege.access.BuiltInAction.WATCH_CHANGES;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 29/02/16
 */
public class GetEntityWatchesActionHandler extends AbstractProjectActionHandler<GetEntityWatchesAction, GetEntityWatchesResult> {

    @Nonnull
    private final WatchManager watchManager;

    @Inject
    public GetEntityWatchesActionHandler(@Nonnull AccessManager accessManager,
                                         @Nonnull WatchManager watchManager) {
        super(accessManager);
        this.watchManager = watchManager;
    }

    @Nonnull
    @Override
    public Class<GetEntityWatchesAction> getActionClass() {
        return GetEntityWatchesAction.class;
    }

    @Nonnull
    @Override
    protected Iterable<BuiltInAction> getRequiredExecutableBuiltInActions(GetEntityWatchesAction action) {
        return Arrays.asList(WATCH_CHANGES, VIEW_PROJECT);
    }

    @Nonnull
    @Override
    public GetEntityWatchesResult execute(@Nonnull GetEntityWatchesAction action, @Nonnull ExecutionContext executionContext) {
        Set<Watch> watches = watchManager.getDirectWatches(action.getEntity(), action.getUserId());
        return GetEntityWatchesResult.create(watches);
    }
}
