package edu.stanford.protege.webprotege.obo;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.dispatch.ExecutionContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Jun 2017
 */
public class GetOboTermIdActionHandler extends AbstractProjectActionHandler<GetOboTermIdAction, GetOboTermIdResult> {

    @Nonnull
    private final TermIdManager manager;

    @Inject
    public GetOboTermIdActionHandler(@Nonnull AccessManager accessManager,
                                     @Nonnull TermIdManager manager) {
        super(accessManager);
        this.manager = manager;
    }

    @Nonnull
    @Override
    public Class<GetOboTermIdAction> getActionClass() {
        return GetOboTermIdAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(GetOboTermIdAction action) {
        return BuiltInAction.VIEW_PROJECT;
    }

    @Nonnull
    @Override
    public GetOboTermIdResult execute(@Nonnull GetOboTermIdAction action, @Nonnull ExecutionContext executionContext) {
        return new GetOboTermIdResult(action.term(), manager.getTermId(action.term()));
    }
}
