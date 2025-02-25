
package edu.stanford.protege.webprotege.metaproject;

import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.ipc.CommandExecutor;
import edu.stanford.protege.webprotege.user.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
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
    private CommandExecutor<UsersQueryRequest, UsersQueryResponse> getUsersExecutor;

    private UserId userId = edu.stanford.protege.webprotege.MockingUtils.mockUserId();

    @BeforeEach
    public void setUp()
    {
        userRecords.clear();

        when(userRecord.getUserId()).thenReturn(userId);
        when(userRecord.getEmailAddress()).thenReturn(email);
        userRecords.add(userRecord);

        when(userRecordRepository.findOne(userId)).thenReturn(java.util.Optional.of(userRecord));
        when(userRecordRepository.findOneByEmailAddress(email)).thenReturn(java.util.Optional.of(userRecord));

        userDetailsManagerImpl = new UserDetailsManagerImpl(userRecordRepository, getUsersExecutor);
    }

    @Test
public void shouldThrowNullPointerExceptionIf_Repository_IsNull() {
    assertThrows(NullPointerException.class, () -> { 
        new UserDetailsManagerImpl(null, getUsersExecutor);
     });
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
