package edu.stanford.protege.webprotege.revision;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInCapability;
import edu.stanford.protege.webprotege.common.Page;
import edu.stanford.protege.webprotege.common.PageCollector;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import jakarta.inject.Inject;

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
    protected BuiltInCapability getRequiredExecutableBuiltInAction(GetRevisionSummariesAction action) {
        return BuiltInCapability.VIEW_CHANGES;
    }

    @Nonnull
    @Override
    public GetRevisionSummariesResult execute(@Nonnull GetRevisionSummariesAction action, @Nonnull ExecutionContext executionContext) {
        var revisionSummaries = revisionManager.getRevisionSummaries();
        var summariesPage = revisionSummaries.stream()
                .collect(PageCollector.toPage(action.pageRequest().getPageNumber(),
                                              action.pageRequest().getPageSize()));
        return new GetRevisionSummariesResult(summariesPage.orElse(Page.emptyPage()));
    }

    @Nonnull
    @Override
    public Class<GetRevisionSummariesAction> getActionClass() {
        return GetRevisionSummariesAction.class;
    }
}
