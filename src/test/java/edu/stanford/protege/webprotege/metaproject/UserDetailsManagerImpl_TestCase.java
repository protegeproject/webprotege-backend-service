
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
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
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

    @Test
    public void shouldReturnEmptyWhenUserIsConfirmedAbsent() throws Exception {
        UsersQueryResponse response = new UsersQueryResponse(new ArrayList<>());
        CompletableFuture<UsersQueryResponse> future = mock(CompletableFuture.class);
        when(future.get(3, TimeUnit.SECONDS)).thenReturn(response);
        when(getUsersExecutor.execute(any(), any())).thenReturn(future);

        assertThat(userDetailsManagerImpl.getUserByUserIdOrEmail("unknown-user"), is(Optional.empty()));
    }

    @Test
    public void shouldReturnUserIdWhenSingleMatchIsFound() throws Exception {
        UsersQueryResponse response = new UsersQueryResponse(List.of(userId));
        CompletableFuture<UsersQueryResponse> future = mock(CompletableFuture.class);
        when(future.get(3, TimeUnit.SECONDS)).thenReturn(response);
        when(getUsersExecutor.execute(any(), any())).thenReturn(future);

        assertThat(userDetailsManagerImpl.getUserByUserIdOrEmail(userId.id()), is(Optional.of(userId)));
    }

    @Test
    public void shouldThrowRuntimeExceptionWhenDuplicateMatchIsFound() throws Exception {
        UserId otherUserId = edu.stanford.protege.webprotege.MockingUtils.mockUserId();
        UsersQueryResponse response = new UsersQueryResponse(List.of(userId, otherUserId));
        CompletableFuture<UsersQueryResponse> future = mock(CompletableFuture.class);
        when(future.get(3, TimeUnit.SECONDS)).thenReturn(response);
        when(getUsersExecutor.execute(any(), any())).thenReturn(future);

        assertThrows(RuntimeException.class,
                      () -> userDetailsManagerImpl.getUserByUserIdOrEmail(userId.id()));
    }

    @Test
    public void shouldThrowUserLookupExceptionOnTimeout() throws Exception {
        CompletableFuture<UsersQueryResponse> future = mock(CompletableFuture.class);
        when(future.get(3, TimeUnit.SECONDS)).thenThrow(new TimeoutException("Timed out"));
        when(getUsersExecutor.execute(any(), any())).thenReturn(future);

        assertThrows(UserLookupException.class,
                      () -> userDetailsManagerImpl.getUserByUserIdOrEmail(userId.id()));
    }

    @Test
    public void shouldThrowUserLookupExceptionOnRpcOrMessagingFailure() throws Exception {
        CompletableFuture<UsersQueryResponse> future = mock(CompletableFuture.class);
        when(future.get(3, TimeUnit.SECONDS)).thenThrow(new ExecutionException(new RuntimeException("RPC failure")));
        when(getUsersExecutor.execute(any(), any())).thenReturn(future);

        assertThrows(UserLookupException.class,
                      () -> userDetailsManagerImpl.getUserByUserIdOrEmail(userId.id()));
    }
}
