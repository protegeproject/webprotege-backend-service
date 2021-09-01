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
public class SetOboTermXRefsActionHandler extends AbstractProjectActionHandler<SetOboTermXRefsAction, SetOboTermXRefsResult> {

    @Nonnull
    private final TermXRefsManager xRefsManager;

    @Inject
    public SetOboTermXRefsActionHandler(@Nonnull AccessManager accessManager,
                                        @Nonnull TermXRefsManager xRefsManager) {
        super(accessManager);
        this.xRefsManager = xRefsManager;
    }

    @Nonnull
    @Override
    public Class<SetOboTermXRefsAction> getActionClass() {
        return SetOboTermXRefsAction.class;
    }

    @Nonnull
    @Override
    public SetOboTermXRefsResult execute(@Nonnull SetOboTermXRefsAction action, @Nonnull ExecutionContext executionContext) {
        xRefsManager.setXRefs(executionContext.getUserId(),
                              action.entity(),
                              action.xrefs());
        return new SetOboTermXRefsResult();
    }
}
