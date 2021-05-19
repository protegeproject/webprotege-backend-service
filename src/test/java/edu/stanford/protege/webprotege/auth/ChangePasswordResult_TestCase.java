package edu.stanford.protege.webprotege.auth;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
public class ChangePasswordResult_TestCase {


    private ChangePasswordResult result;

    private ChangePasswordResult otherResult;

    private AuthenticationResponse authenticationResponse = AuthenticationResponse.SUCCESS;


    @Before
    public void setUp() throws Exception {
        result = ChangePasswordResult.create(authenticationResponse);
        otherResult = ChangePasswordResult.create(authenticationResponse);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerException() {
        ChangePasswordResult.create(null);
    }

    @Test
    public void shouldReturnSuppliedResponse() {
        assertThat(result.getResponse(), is(authenticationResponse));
    }
}