package edu.stanford.protege.webprotege.auth;

import edu.stanford.protege.webprotege.user.UserId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19/02/15
 */
@RunWith(MockitoJUnitRunner.class)
public class ChangePasswordAction_TestCase {


    private ChangePasswordAction action;

    @Mock
    private UserId userId;

    @Mock
    private Password currentPassword, newPassword;


    @Before
    public void setUp() throws Exception {
        action = ChangePasswordAction.create(userId, currentPassword, newPassword);
    }

    @Test
    public void shouldGetSuppliedCurrentPassword() {
        assertThat(action.getCurrentPassword(), is(currentPassword));
    }

    @Test
    public void shouldGetSuppliedNewPassword() {
        assertThat(action.getNewPassword(), is(newPassword));
    }
}