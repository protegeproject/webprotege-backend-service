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
public class GetOboTermCrossProductsActionHandler extends AbstractProjectActionHandler<GetOboTermCrossProductAction, GetOboTermCrossProductResult> {

    @Nonnull
    private final TermCrossProductsManager crossProductsManager;

    @Inject
    public GetOboTermCrossProductsActionHandler(@Nonnull AccessManager accessManager,
                                                @Nonnull TermCrossProductsManager crossProductsManager) {
        super(accessManager);
        this.crossProductsManager = crossProductsManager;
    }

    @Nonnull
    @Override
    public Class<GetOboTermCrossProductAction> getActionClass() {
        return GetOboTermCrossProductAction.class;
    }

    @Nonnull
    @Override
    public GetOboTermCrossProductResult execute(@Nonnull GetOboTermCrossProductAction action,
                                                @Nonnull ExecutionContext executionContext) {
        OBOTermCrossProduct crossProduct = crossProductsManager.getCrossProduct(action.getEntity());
        return GetOboTermCrossProductResult.create(crossProduct);
    }
}
