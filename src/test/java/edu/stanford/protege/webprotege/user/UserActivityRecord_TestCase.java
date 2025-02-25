
package edu.stanford.protege.webprotege.user;

import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.project.RecentProjectRecord;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserActivityRecord_TestCase {

    private UserActivityRecord userActivityRecord;

    private UserId userId = edu.stanford.protege.webprotege.MockingUtils.mockUserId();

    private long lastLogin = 1L;

    private long lastLogout = 2L;

    @Mock
    private RecentProjectRecord recentProject;

    private List<RecentProjectRecord> recentProjects;

    @BeforeEach
    public void setUp() {
        recentProjects = singletonList(recentProject);
        userActivityRecord = new UserActivityRecord(userId, lastLogin, lastLogout, recentProjects);
    }

    @SuppressWarnings("ConstantConditions")
    @Test
public void shouldThrowNullPointerExceptionIf_userId_IsNull() {
    assertThrows(NullPointerException.class, () -> { 
        new UserActivityRecord(null, lastLogin, lastLogout, recentProjects);
     });
}

    @Test
    public void shouldReturnSupplied_userId() {
        assertThat(userActivityRecord.getUserId(), is(this.userId));
    }

    @Test
    public void shouldReturnSupplied_lastLogin() {
        assertThat(userActivityRecord.getLastLogin(), is(this.lastLogin));
    }

    @Test
    public void shouldReturnSupplied_lastLogout() {
        assertThat(userActivityRecord.getLastLogout(), is(this.lastLogout));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
public void shouldThrowNullPointerExceptionIf_recentProjects_IsNull() {
    assertThrows(NullPointerException.class, () -> { 
        new UserActivityRecord(userId, lastLogin, lastLogout, null);
     });
}

    @Test
    public void shouldReturnSupplied_recentProjects() {
        assertThat(userActivityRecord.getRecentProjects(), is(this.recentProjects));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(userActivityRecord, is(userActivityRecord));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(userActivityRecord.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(userActivityRecord, is(new UserActivityRecord(userId, lastLogin, lastLogout, recentProjects)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_userId() {
        assertThat(userActivityRecord, is(not(new UserActivityRecord(edu.stanford.protege.webprotege.MockingUtils.mockUserId(), lastLogin, lastLogout, recentProjects))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_lastLogin() {
        assertThat(userActivityRecord, is(not(new UserActivityRecord(userId, 3L, lastLogout, recentProjects))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_lastLogout() {
        assertThat(userActivityRecord, is(not(new UserActivityRecord(userId, lastLogin, 4L, recentProjects))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_recentProjects() {
        assertThat(userActivityRecord, is(not(new UserActivityRecord(userId, lastLogin, lastLogout,
                                                                     singletonList(mock(RecentProjectRecord.class))))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(userActivityRecord.hashCode(), is(new UserActivityRecord(userId, lastLogin, lastLogout, recentProjects).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(userActivityRecord.toString(), Matchers.startsWith("UserActivityRecord"));
    }
}
