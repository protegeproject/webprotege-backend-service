package edu.stanford.protege.webprotege.dispatch;

import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/02/15
 */
@RunWith(MockitoJUnitRunner.class)
public class ExecutionContext_TestCase {

    private UserId userId = UserId.valueOf("TheUser");

    private ExecutionContext context;



    @Before
    public void setUp() throws Exception {
        context = new ExecutionContext(userId, "");
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerForNullSession() {
        new ExecutionContext(null, null);
    }

    @Test
    public void shouldReturnUserId() {
        assertThat(context.userId(), is(userId));
    }

}
