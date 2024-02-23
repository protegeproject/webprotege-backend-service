package edu.stanford.protege.webprotege.dispatch.handlers;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.dispatch.actions.GetRevisionsAction;
import edu.stanford.protege.webprotege.dispatch.actions.GetRevisionsResult;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.revision.Revision;
import edu.stanford.protege.webprotege.revision.RevisionDetails;
import edu.stanford.protege.webprotege.revision.RevisionDetailsExtractor;
import edu.stanford.protege.webprotege.revision.RevisionManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.function.Predicate;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23 Apr 2018
 */
public class GetRevisionsActionHandler extends AbstractProjectActionHandler<GetRevisionsAction, GetRevisionsResult> {

    private final RevisionManager revisionManager;

    private final RevisionDetailsExtractor extractor;

    @Inject
    public GetRevisionsActionHandler(@Nonnull AccessManager accessManager,
                                     @Nonnull RevisionManager revisionManager, RevisionDetailsExtractor extractor) {
        super(accessManager);
        this.revisionManager = checkNotNull(revisionManager);
        this.extractor = extractor;
    }

    @Nonnull
    @Override
    public Class<GetRevisionsAction> getActionClass() {
        return GetRevisionsAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(GetRevisionsAction action) {
        return BuiltInAction.VIEW_CHANGES;
    }

    @Nonnull
    @Override
    public GetRevisionsResult execute(@Nonnull GetRevisionsAction action,
                                      @Nonnull ExecutionContext executionContext) {
        long fromIndex = action.getFrom().getValue() - 1;
        long skip = fromIndex;
        long limit = action.getTo().getValue() - fromIndex;
        Predicate<Revision> byAuthor = rev -> !action.getAuthor().isPresent() || action.getAuthor().get().equals(rev.getUserId());
        ImmutableList<RevisionDetails> revisionDetails = revisionManager.getRevisions().stream()
                                                                        .skip(skip)
                                                                        .limit(limit)
                                                                        .filter(byAuthor)
                                                                        .map(extractor::extractRevisionDetails)
                                                                        .collect(ImmutableList.toImmutableList());
        return new GetRevisionsResult(revisionDetails,
                                      revisionManager.getCurrentRevision());
    }
}
