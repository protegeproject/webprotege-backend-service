package edu.stanford.protege.webprotege.obo;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.dispatch.ExecutionContext;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23 Jun 2017
 */
public class SetOboTermCrossProductsActionHandler extends AbstractProjectActionHandler<SetOboTermCrossProductAction, SetOboTermCrossProductResult> {

    @Nonnull
    private final TermCrossProductsManager crossProductsManager;

    @Inject
    public SetOboTermCrossProductsActionHandler(@Nonnull AccessManager accessManager,
                                                @Nonnull TermCrossProductsManager crossProductsManager) {
        super(accessManager);
        this.crossProductsManager = crossProductsManager;
    }

    @Nonnull
    @Override
    public Class<SetOboTermCrossProductAction> getActionClass() {
        return SetOboTermCrossProductAction.class;
    }

    @Nonnull
    @Override
    public SetOboTermCrossProductResult execute(@Nonnull SetOboTermCrossProductAction action,
                                                @Nonnull ExecutionContext executionContext) {
        crossProductsManager.setCrossProduct(executionContext.getUserId(),
                                             action.term(),
                                             action.crossProduct());
        return new SetOboTermCrossProductResult();
    }
}
