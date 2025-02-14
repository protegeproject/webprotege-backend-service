
package edu.stanford.protege.webprotege.user;

import edu.stanford.protege.webprotege.common.UserId;
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
public class UserRecord_TestCase {

    private UserRecord userRecord;

    private UserId userId = edu.stanford.protege.webprotege.MockingUtils.mockUserId();

    private String realName = "The realName";

    private String emailAddress = "The emailAddress";

    private String avatarUrl = "The avatarUrl";

    @BeforeEach
    public void setUp() {
        userRecord = new UserRecord(userId, realName, emailAddress, avatarUrl);
    }

    @Test
public void shouldThrowNullPointerExceptionIf_userId_IsNull() {
    assertThrows(NullPointerException.class, () -> {
        new UserRecord(null, realName, emailAddress, avatarUrl);
     });
}

    @Test
    public void shouldReturnSupplied_userId() {
        assertThat(userRecord.getUserId(), is(this.userId));
    }

    @Test
public void shouldThrowNullPointerExceptionIf_realName_IsNull() {
    assertThrows(NullPointerException.class, () -> {
        new UserRecord(userId, null, emailAddress, avatarUrl);
     });
}

    @Test
    public void shouldReturnSupplied_realName() {
        assertThat(userRecord.getRealName(), is(this.realName));
    }

    @Test
public void shouldThrowNullPointerExceptionIf_emailAddress_IsNull() {
    assertThrows(NullPointerException.class, () -> {
        new UserRecord(userId, realName, null, avatarUrl);
     });
}

    @Test
    public void shouldReturnSupplied_emailAddress() {
        assertThat(userRecord.getEmailAddress(), is(this.emailAddress));
    }

    @Test
public void shouldThrowNullPointerExceptionIf_avatarUrl_IsNull() {
    assertThrows(NullPointerException.class, () -> {
        new UserRecord(userId, realName, emailAddress, null);
     });
}

    @Test
    public void shouldReturnSupplied_avatarUrl() {
        assertThat(userRecord.getAvatarUrl(), is(this.avatarUrl));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(userRecord, is(userRecord));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(userRecord.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(userRecord, is(new UserRecord(userId, realName, emailAddress, avatarUrl)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_userId() {
        assertThat(userRecord, is(not(new UserRecord(edu.stanford.protege.webprotege.MockingUtils.mockUserId(), realName, emailAddress, avatarUrl))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_realName() {
        assertThat(userRecord, is(not(new UserRecord(userId, "String-c546a2c3-850a-40db-81c2-d376303cb542", emailAddress, avatarUrl))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_emailAddress() {
        assertThat(userRecord, is(not(new UserRecord(userId, realName, "String-e725a477-c521-469e-abff-3cf9dcdfd977", avatarUrl))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_avatarUrl() {
        assertThat(userRecord, is(not(new UserRecord(userId, realName, emailAddress, "String-9c62bcba-f234-4c15-aa49-650fe22b962f"))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(userRecord.hashCode(), is(new UserRecord(userId, realName, emailAddress, avatarUrl).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(userRecord.toString(), Matchers.startsWith("UserRecord"));
    }

}
