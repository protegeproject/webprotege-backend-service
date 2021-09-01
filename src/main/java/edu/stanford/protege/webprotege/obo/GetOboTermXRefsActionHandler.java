package edu.stanford.protege.webprotege.obo;

import com.google.common.collect.ImmutableList;
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
public class GetOboTermXRefsActionHandler extends AbstractProjectActionHandler<GetOboTermXRefsAction, GetOboTermXRefsResult> {

    @Nonnull
    private final TermXRefsManager xRefsManager;

    @Inject
    public GetOboTermXRefsActionHandler(@Nonnull AccessManager accessManager,
                                        @Nonnull TermXRefsManager xRefsManager) {
        super(accessManager);
        this.xRefsManager = xRefsManager;
    }

    @Nonnull
    @Override
    public Class<GetOboTermXRefsAction> getActionClass() {
        return GetOboTermXRefsAction.class;
    }

    @Nonnull
    @Override
    public GetOboTermXRefsResult execute(@Nonnull GetOboTermXRefsAction action, @Nonnull ExecutionContext executionContext) {
        return new GetOboTermXRefsResult(ImmutableList.copyOf(xRefsManager.getXRefs(action.entity())));
    }
}
