package edu.stanford.protege.webprotege.access;


import edu.stanford.protege.webprotege.authorization.ActionId;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ActionId_TestCase {

    private ActionId actionId;

    private String id = "The id";

    @BeforeEach
    public void setUp() {
        actionId = new ActionId(id);
    }

    @SuppressWarnings("ConstantConditions")
    @Test
public void shouldThrowNullPointerExceptionIf_id_IsNull() {
    assertThrows(java.lang.NullPointerException.class, () -> { 
        new ActionId(null);
     });
}

    @Test
    public void shouldReturnSupplied_id() {
        assertThat(actionId.id(), is(this.id));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(actionId, is(actionId));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(actionId.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(actionId, is(new ActionId(id)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_id() {
        assertThat(actionId, is(not(new ActionId("String-49f80fc5-f0c4-4013-accc-4f37f60d5632"))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(actionId.hashCode(), is(new ActionId(id).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(actionId.toString(), Matchers.startsWith("ActionId"));
    }

}
