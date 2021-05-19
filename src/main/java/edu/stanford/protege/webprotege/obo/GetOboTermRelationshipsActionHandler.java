package edu.stanford.protege.webprotege.obo;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.dispatch.ExecutionContext;
import edu.stanford.protege.webprotege.access.BuiltInAction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Jun 2017
 */
public class GetOboTermRelationshipsActionHandler extends AbstractProjectActionHandler<GetOboTermRelationshipsAction, GetOboTermRelationshipsResult> {

    @Nonnull
    private final TermRelationshipsManager relationshipsManager;

    @Inject
    public GetOboTermRelationshipsActionHandler(@Nonnull AccessManager accessManager,
                                                @Nonnull TermRelationshipsManager relationshipsManager) {
        super(accessManager);
        this.relationshipsManager = relationshipsManager;
    }

    @Nonnull
    @Override
    public Class<GetOboTermRelationshipsAction> getActionClass() {
        return GetOboTermRelationshipsAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(GetOboTermRelationshipsAction action) {
        return BuiltInAction.VIEW_PROJECT;
    }

    @Nonnull
    @Override
    public GetOboTermRelationshipsResult execute(@Nonnull GetOboTermRelationshipsAction action,
                                                 @Nonnull ExecutionContext executionContext) {
        return GetOboTermRelationshipsResult.create(relationshipsManager.getRelationships(action.getEntity().asOWLClass()));
    }
}
