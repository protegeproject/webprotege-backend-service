package edu.stanford.protege.webprotege.dispatch.validators;

import edu.stanford.protege.webprotege.dispatch.RequestContext;
import edu.stanford.protege.webprotege.dispatch.RequestValidationResult;
import edu.stanford.protege.webprotege.dispatch.Action;
import edu.stanford.protege.webprotege.common.UserId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/02/15
 */
@RunWith(MockitoJUnitRunner.class)
public class UserIsSignedInValidator_TestCase<A extends Action<?>> {

    private UserIsSignedInValidator validator;

    @Mock
    private A action;

    @Mock
    private RequestContext requestContext;

    private UserId userId = edu.stanford.protege.webprotege.MockingUtils.mockUserId();


    @Test
    public void shouldValidateUserThatIsNotGuest() {
        validator = new UserIsSignedInValidator(userId);
        RequestValidationResult result = validator.validateAction();
        assertThat(result.isValid(), is(true));
    }

    @Test
    public void shouldNoValidateUserThatIsGuest() {
        validator = new UserIsSignedInValidator(UserId.getGuest());
        RequestValidationResult result = validator.validateAction();
        assertThat(result.isValid(), is(false));
    }
}
