package edu.stanford.protege.webprotege.itemlist;

import edu.stanford.protege.webprotege.dispatch.ApplicationActionHandler;
import edu.stanford.protege.webprotege.dispatch.ExecutionContext;
import edu.stanford.protege.webprotege.dispatch.RequestContext;
import edu.stanford.protege.webprotege.dispatch.RequestValidator;
import edu.stanford.protege.webprotege.dispatch.validators.NullValidator;
import edu.stanford.protege.webprotege.sharing.PersonId;
import edu.stanford.protege.webprotege.user.UserDetailsManager;
import edu.stanford.protege.webprotege.common.UserId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13/05/15
 */
public class GetPersonIdItemsActionHandler implements ApplicationActionHandler<GetPersonIdItemsAction, GetPersonIdItemsResult> {


    private final UserDetailsManager userDetailsManager;

    @Inject
    public GetPersonIdItemsActionHandler(UserDetailsManager userDetailsManager) {
        this.userDetailsManager = checkNotNull(userDetailsManager);
    }

    @Nonnull
    @Override
    public Class<GetPersonIdItemsAction> getActionClass() {
        return GetPersonIdItemsAction.class;
    }

    @Nonnull
    @Override
    public RequestValidator getRequestValidator(@Nonnull GetPersonIdItemsAction action, @Nonnull RequestContext requestContext) {
        return NullValidator.get();
    }

    @Nonnull
    @Override
    public GetPersonIdItemsResult execute(@Nonnull GetPersonIdItemsAction action, @Nonnull ExecutionContext executionContext) {
        List<PersonId> result = new ArrayList<>();
        for(String itemName : action.getItemNames()) {
            if(userDetailsManager.getUserDetails(UserId.valueOf(itemName)).isPresent()) {
                result.add(PersonId.get(itemName));
            }
        }
        return new GetPersonIdItemsResult(result);
    }
}
