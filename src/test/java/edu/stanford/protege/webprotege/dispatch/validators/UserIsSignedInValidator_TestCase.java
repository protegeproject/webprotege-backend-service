package edu.stanford.protege.webprotege.dispatch.validators;

import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.dispatch.Action;
import edu.stanford.protege.webprotege.dispatch.RequestContext;
import edu.stanford.protege.webprotege.dispatch.RequestValidationResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/02/15
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
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
