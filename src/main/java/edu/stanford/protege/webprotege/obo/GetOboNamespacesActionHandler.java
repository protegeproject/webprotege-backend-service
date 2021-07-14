package edu.stanford.protege.webprotege.obo;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.dispatch.ExecutionContext;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Jun 2017
 */
public class GetOboNamespacesActionHandler extends AbstractProjectActionHandler<GetOboNamespacesAction, GetOboNamespacesResult> {

    @Nonnull
    private final OBONamespaceCache cache;

    @Inject
    public GetOboNamespacesActionHandler(@Nonnull AccessManager accessManager,
                                         @Nonnull OBONamespaceCache cache) {
        super(accessManager);
        this.cache = checkNotNull(cache);
    }

    @Nonnull
    @Override
    public Class<GetOboNamespacesAction> getActionClass() {
        return GetOboNamespacesAction.class;
    }

    @Nonnull
    @Override
    public GetOboNamespacesResult execute(@Nonnull GetOboNamespacesAction action, @Nonnull ExecutionContext executionContext) {
        return GetOboNamespacesResult.create(cache.getNamespaces());
    }
}
