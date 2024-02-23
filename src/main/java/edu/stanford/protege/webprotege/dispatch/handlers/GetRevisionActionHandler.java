package edu.stanford.protege.webprotege.dispatch.handlers;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.dispatch.actions.GetRevisionAction;
import edu.stanford.protege.webprotege.dispatch.actions.GetRevisionResult;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.revision.Revision;
import edu.stanford.protege.webprotege.revision.RevisionDetails;
import edu.stanford.protege.webprotege.revision.RevisionDetailsExtractor;
import edu.stanford.protege.webprotege.revision.RevisionManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 9 May 2018
 */
public class GetRevisionActionHandler extends AbstractProjectActionHandler<GetRevisionAction, GetRevisionResult> {

    @Nonnull
    private final RevisionManager revisionManager;

    @Nonnull
    private final RevisionDetailsExtractor extractor;

    @Inject
    public GetRevisionActionHandler(@Nonnull AccessManager accessManager,
                                    @Nonnull RevisionManager revisionManager,
                                    @Nonnull RevisionDetailsExtractor extractor) {
        super(accessManager);
        this.revisionManager = checkNotNull(revisionManager);
        this.extractor = checkNotNull(extractor);
    }

    @Nonnull
    @Override
    public Class<GetRevisionAction> getActionClass() {
        return GetRevisionAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(GetRevisionAction action) {
        return BuiltInAction.VIEW_CHANGES;
    }

    @Nonnull
    @Override
    public GetRevisionResult execute(@Nonnull GetRevisionAction action, @Nonnull ExecutionContext executionContext) {
        Optional<Revision> revision = revisionManager.getRevision(action.getRevisionNumber());
        Optional<RevisionDetails> details = revision.map(extractor::extractRevisionDetails);
        return new GetRevisionResult(details);
    }
}
