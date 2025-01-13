
package edu.stanford.protege.webprotege.mail;

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
public class MessageId_TestCase {

    private MessageId messageId;

    private String id = "The id";

    @BeforeEach
    public void setUp() {
        messageId = new MessageId(id);
    }

    @SuppressWarnings("ConstantConditions")
    @Test
public void shouldThrowNullPointerExceptionIf_id_IsNull() {
    assertThrows(NullPointerException.class, () -> { 
        new MessageId(null);
     });
}

    @Test
    public void shouldReturnSupplied_id() {
        assertThat(messageId.getId(), is(this.id));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(messageId, is(messageId));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(messageId.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(messageId, is(new MessageId(id)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_id() {
        assertThat(messageId, is(not(new MessageId("String-a556b7cd-f500-442b-9598-06460f3f48cb"))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(messageId.hashCode(), is(new MessageId(id).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(messageId.toString(), Matchers.startsWith("MessageId"));
    }

}
