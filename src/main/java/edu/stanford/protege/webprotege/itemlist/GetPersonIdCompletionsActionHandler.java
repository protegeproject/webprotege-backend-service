package edu.stanford.protege.webprotege.itemlist;

import edu.stanford.protege.webprotege.dispatch.ApplicationActionHandler;
import edu.stanford.protege.webprotege.dispatch.ExecutionContext;
import edu.stanford.protege.webprotege.dispatch.RequestContext;
import edu.stanford.protege.webprotege.dispatch.RequestValidator;
import edu.stanford.protege.webprotege.dispatch.validators.NullValidator;
import edu.stanford.protege.webprotege.sharing.PersonId;
import edu.stanford.protege.webprotege.user.UserDetailsManager;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/05/15
 */
public class GetPersonIdCompletionsActionHandler implements ApplicationActionHandler<GetPersonIdCompletionsAction, GetPossibleItemCompletionsResult<PersonId>> {


    private final UserDetailsManager userDetailsManager;

    @Inject
    public GetPersonIdCompletionsActionHandler(UserDetailsManager userDetailsManager) {
        this.userDetailsManager = checkNotNull(userDetailsManager);
    }

    @Nonnull
    @Override
    public Class<GetPersonIdCompletionsAction> getActionClass() {
        return GetPersonIdCompletionsAction.class;
    }

    @Nonnull
    @Override
    public RequestValidator getRequestValidator(@Nonnull GetPersonIdCompletionsAction action, @Nonnull RequestContext requestContext) {
        return NullValidator.get();
    }

    @Nonnull
    @Override
    public GetPersonIdCompletionsResult execute(@Nonnull GetPersonIdCompletionsAction action, @Nonnull ExecutionContext executionContext) {
        List<PersonId> matches = userDetailsManager.getUserIdsContainingIgnoreCase(action.getCompletionText(), 7).stream()
                .map(u -> PersonId.get(u.getUserName()))
                .collect(toList());
        return GetPersonIdCompletionsResult.create(matches);
    }
}
