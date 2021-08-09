
package edu.stanford.protege.webprotege.metaproject;

import edu.stanford.protege.webprotege.user.UserDetailsManagerImpl;
import edu.stanford.protege.webprotege.user.UserRecord;
import edu.stanford.protege.webprotege.user.UserRecordRepository;
import edu.stanford.protege.webprotege.user.EmailAddress;
import edu.stanford.protege.webprotege.common.UserId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@RunWith(org.mockito.junit.MockitoJUnitRunner.class)
public class UserDetailsManagerImpl_TestCase {

    private UserDetailsManagerImpl userDetailsManagerImpl;

    private String userName = "THE USER NAME";

    private String email = "THE EMAIL";

    @Mock
    private UserRecordRepository userRecordRepository;

    private List<UserRecord> userRecords = new ArrayList<>();

    @Mock
    private UserRecord userRecord;

    @Mock
    private UserId userId;

    @Before
    public void setUp()
    {
        userRecords.clear();

        when(userRecord.getUserId()).thenReturn(userId);
        when(userRecord.getEmailAddress()).thenReturn(email);
        userRecords.add(userRecord);

        when(userRecordRepository.findOne(userId)).thenReturn(java.util.Optional.of(userRecord));
        when(userRecordRepository.findOneByEmailAddress(email)).thenReturn(java.util.Optional.of(userRecord));

        userDetailsManagerImpl = new UserDetailsManagerImpl(userRecordRepository);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_Repository_IsNull() {
        new UserDetailsManagerImpl(null);
    }

    @Test
    public void should_getEmail() {
        assertThat(userDetailsManagerImpl.getEmail(userId), is(java.util.Optional.of(email)));
    }

    @Test
    public void shouldGetUserIdByEmail() {
        EmailAddress emailAddress = new EmailAddress(email);
        assertThat(userDetailsManagerImpl.getUserIdByEmailAddress(emailAddress), is(java.util.Optional.of(userId)));
    }
}
