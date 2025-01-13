package edu.stanford.protege.webprotege.dispatch;

import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/02/15
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ExecutionContext_TestCase {

    private UserId userId = UserId.valueOf("TheUser");

    private ExecutionContext context;



    @BeforeEach
    public void setUp() throws Exception {
        context = new ExecutionContext(userId, "");
    }

    @Test
    public void shouldReturnGuestUserIdForNullSession() {
        ExecutionContext ec = new ExecutionContext();
        Assert.assertTrue(ec.userId().isGuest());
    }

    @Test
    public void shouldReturnUserId() {
        assertThat(context.userId(), is(userId));
    }

}
