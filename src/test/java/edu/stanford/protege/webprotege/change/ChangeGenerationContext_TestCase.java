package edu.stanford.protege.webprotege.change;

import edu.stanford.protege.webprotege.common.UserId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-29
 */
@RunWith(MockitoJUnitRunner.class)
public class ChangeGenerationContext_TestCase {

    private ChangeGenerationContext context;

    private UserId userId = edu.stanford.protege.webprotege.MockingUtils.mockUserId();

    @Before
    public void setUp() {
        context = new ChangeGenerationContext(userId);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeIfUserIdIsNull() {
        new ChangeGenerationContext(null);
    }

    @Test
    public void shouldGetUserId() {
        assertThat(context.getUserId(), is(userId));
    }
}
