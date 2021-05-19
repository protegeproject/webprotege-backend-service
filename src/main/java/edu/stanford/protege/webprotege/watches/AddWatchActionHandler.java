package edu.stanford.protege.webprotege.watches;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.dispatch.ExecutionContext;
import edu.stanford.protege.webprotege.dispatch.RequestContext;
import edu.stanford.protege.webprotege.dispatch.RequestValidator;
import edu.stanford.protege.webprotege.dispatch.validators.NullValidator;
import edu.stanford.protege.webprotege.events.EventManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.event.EventTag;
import edu.stanford.protege.webprotege.event.ProjectEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/03/2013
 */
public class AddWatchActionHandler extends AbstractProjectActionHandler<AddWatchAction, AddWatchResult> {

    private EventManager<ProjectEvent<?>> eventManager;

    private WatchManager watchManager;

    @Inject
    public AddWatchActionHandler(@Nonnull AccessManager accessManager,
                                 EventManager<ProjectEvent<?>> eventManager,
                                 WatchManager watchManager) {
        super(accessManager);
        this.eventManager = eventManager;
        this.watchManager = watchManager;
    }

    @Nonnull
    @Override
    public Class<AddWatchAction> getActionClass() {
        return AddWatchAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(AddWatchAction action) {
        return BuiltInAction.WATCH_CHANGES;
    }

    @Nonnull
    @Override
    protected RequestValidator getAdditionalRequestValidator(AddWatchAction action, RequestContext requestContext) {
        return NullValidator.get();
    }

    @Nonnull
    @Override
    public AddWatchResult execute(@Nonnull AddWatchAction action, @Nonnull ExecutionContext executionContext) {
        EventTag startTag = eventManager.getCurrentTag();
        watchManager.addWatch(action.getWatch());
        return new AddWatchResult(eventManager.getEventsFromTag(startTag));
    }
}
