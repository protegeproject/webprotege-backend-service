package edu.stanford.protege.webprotege.revision;

import com.google.common.collect.ImmutableList;
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
 * 21/02/15
 */
public class GetRevisionSummariesActionHandler extends AbstractProjectActionHandler<GetRevisionSummariesAction, GetRevisionSummariesResult> {

    @Nonnull
    private final RevisionManager revisionManager;

    @Inject
    public GetRevisionSummariesActionHandler(@Nonnull AccessManager accessManager,
                                             @Nonnull RevisionManager revisionManager) {
        super(accessManager);
        this.revisionManager = revisionManager;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(GetRevisionSummariesAction action) {
        return BuiltInAction.VIEW_CHANGES;
    }

    @Nonnull
    @Override
    public GetRevisionSummariesResult execute(@Nonnull GetRevisionSummariesAction action, @Nonnull ExecutionContext executionContext) {
        return new GetRevisionSummariesResult(ImmutableList.copyOf(revisionManager.getRevisionSummaries()));
    }

    @Nonnull
    @Override
    public Class<GetRevisionSummariesAction> getActionClass() {
        return GetRevisionSummariesAction.class;
    }
}
