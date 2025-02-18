package edu.stanford.protege.webprotege.change;

import edu.stanford.protege.webprotege.common.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-29
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ChangeGenerationContext_TestCase {

    private ChangeGenerationContext context;

    private UserId userId = edu.stanford.protege.webprotege.MockingUtils.mockUserId();

    @BeforeEach
    public void setUp() {
        context = new ChangeGenerationContext(userId);
    }

    @Test
public void shouldThrowNpeIfUserIdIsNull() {
    assertThrows(NullPointerException.class, () -> { 
        new ChangeGenerationContext(null);
     });
}

    @Test
    public void shouldGetUserId() {
        assertThat(context.getUserId(), is(userId));
    }
}
