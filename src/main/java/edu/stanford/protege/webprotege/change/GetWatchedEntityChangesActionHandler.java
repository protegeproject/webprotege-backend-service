package edu.stanford.protege.webprotege.change;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.dispatch.ExecutionContext;
import edu.stanford.protege.webprotege.pagination.Page;
import edu.stanford.protege.webprotege.watches.Watch;
import edu.stanford.protege.webprotege.watches.WatchManager;
import edu.stanford.protege.webprotege.watches.WatchedChangesManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Set;

import static edu.stanford.protege.webprotege.access.BuiltInAction.VIEW_CHANGES;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27/02/15
 */
public class GetWatchedEntityChangesActionHandler extends AbstractProjectActionHandler<GetWatchedEntityChangesAction, GetWatchedEntityChangesResult> {

    @Nonnull
    private final WatchManager watchManager;

    @Nonnull
    private final WatchedChangesManager watchedChangesManager;

    @Inject
    public GetWatchedEntityChangesActionHandler(@Nonnull AccessManager accessManager,
                                                @Nonnull WatchManager watchManager,
                                                @Nonnull WatchedChangesManager watchedChangesManager) {
        super(accessManager);
        this.watchManager = watchManager;
        this.watchedChangesManager = watchedChangesManager;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(GetWatchedEntityChangesAction action) {
        return VIEW_CHANGES;
    }

    @Nonnull
    @Override
    public GetWatchedEntityChangesResult execute(@Nonnull GetWatchedEntityChangesAction action, @Nonnull ExecutionContext executionContext) {
        Set<Watch> watches = watchManager.getWatches(action.getUserId());
        ImmutableList<ProjectChange> changes = watchedChangesManager.getProjectChangesForWatches(watches);
        Page<ProjectChange> page = Page.create(1, 1, changes, changes.size());
        return GetWatchedEntityChangesResult.create(page);
    }

    @Nonnull
    @Override
    public Class<GetWatchedEntityChangesAction> getActionClass() {
        return GetWatchedEntityChangesAction.class;
    }
}
