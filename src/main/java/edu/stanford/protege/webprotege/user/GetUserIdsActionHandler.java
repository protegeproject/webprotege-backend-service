package edu.stanford.protege.webprotege.user;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.dispatch.ActionHandler;
import edu.stanford.protege.webprotege.dispatch.ExecutionContext;
import edu.stanford.protege.webprotege.dispatch.RequestContext;
import edu.stanford.protege.webprotege.dispatch.RequestValidator;
import edu.stanford.protege.webprotege.dispatch.validators.NullValidator;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/01/15
 */
public class GetUserIdsActionHandler implements ActionHandler<GetUserIdsAction, GetUserIdsResult> {

    private HasUserIds hasUserIds;

    @Inject
    public GetUserIdsActionHandler(HasUserIds hasUserIds) {
        this.hasUserIds = checkNotNull(hasUserIds);
    }

    @Nonnull
    @Override
    public Class<GetUserIdsAction> getActionClass() {
        return GetUserIdsAction.class;
    }

    @Nonnull
    @Override
    public RequestValidator getRequestValidator(@Nonnull GetUserIdsAction action, @Nonnull RequestContext requestContext) {
        return NullValidator.get();
    }

    @Nonnull
    @Override
    public GetUserIdsResult execute(@Nonnull GetUserIdsAction action, @Nonnull ExecutionContext executionContext) {
        return new GetUserIdsResult(ImmutableList.copyOf(hasUserIds.getUserIds()));
    }
}
