package edu.stanford.protege.webprotege.user;

import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.ipc.CommandExecutor;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 06/02/15
 */

@Component
public class UserDetailsManagerImpl implements UserDetailsManager {

    private final UserRecordRepository repository;

    private final CommandExecutor<UsersQueryRequest, UsersQueryResponse> getUsersExecutor;
    private final Logger logger = LoggerFactory.getLogger(UserDetailsManagerImpl.class);

    public UserDetailsManagerImpl(UserRecordRepository userRecordRepository, CommandExecutor<UsersQueryRequest, UsersQueryResponse> getUsersExecutor) {
        this.repository = checkNotNull(userRecordRepository);
        this.getUsersExecutor = getUsersExecutor;
    }

    @Override
    public List<UserId> getUserIdsContainingIgnoreCase(String userName, int limit) {
        try {
            return getUsersExecutor.execute(new UsersQueryRequest(userName, false), new ExecutionContext()).get(5, TimeUnit.SECONDS).completions();
        } catch (Exception e) {
            logger.error("Error calling get users",e);
            return new ArrayList<>();
        }
    }

    @Override
    public Optional<UserId> getUserIdByEmailAddress(EmailAddress emailAddress) {
        if (emailAddress.getEmailAddress().isEmpty()) {
            return Optional.empty();
        }
        Optional<UserRecord> record = repository.findOneByEmailAddress(emailAddress.getEmailAddress());
        return record.map(UserRecord::getUserId);
    }

    @Override
    public Optional<UserDetails> getUserDetails(UserId userId) {
        if (userId.isGuest()) {
            return Optional.of(UserDetails.getGuestUserDetails());
        }
        Optional<UserRecord> record = repository.findOne(userId);
        return record.map(userRecord ->
                                  UserDetails.getUserDetails(userId,
                                                             userId.id(),
                                                             Optional.of(userRecord.getEmailAddress())));
    }

    @Override
    public Optional<String> getEmail(UserId userId) {
        if (userId.isGuest()) {
            return Optional.empty();
        }
        Optional<UserRecord> record = repository.findOne(userId);
        if (record.isEmpty()) {
            return Optional.empty();
        }
        String emailAddress = record.get().getEmailAddress();
        if (emailAddress.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(emailAddress);
    }

    @Override
    public void setEmail(UserId userId, String email) {
        checkNotNull(userId);
        checkNotNull(email);
        logger.info("Received request to set email address ({}) for user ({})", email, userId.id());
        if (userId.isGuest()) {
            logger.info("Specified user is the guest user.  Not setting email address.");
            return;
        }
        Optional<UserRecord> record = repository.findOne(userId);
        if (record.isEmpty()) {
            logger.info("Specified user ({}) does not exist.", userId.id());
            return;
        }
        Optional<UserRecord> recordByEmail = repository.findOneByEmailAddress(email);
        if (recordByEmail.isPresent()) {
            logger.info("Account with specified email address ({}) already exists", email);
            return;
        }
        UserRecord theRecord = record.get();
        UserRecord replacement = new UserRecord(
                theRecord.getUserId(),
                theRecord.getRealName(),
                email,
                theRecord.getAvatarUrl()
        );
        repository.delete(userId);
        repository.save(replacement);
        logger.info("Email address for {} changed to {}", userId.id(), email);
    }

    @Override
    public Optional<UserId> getUserByUserIdOrEmail(String userNameOrEmail) {
        try{
            List<UserId> response =  getUsersExecutor.execute(new UsersQueryRequest(userNameOrEmail,true), new ExecutionContext()).get(3, TimeUnit.SECONDS).completions();
            if(response == null || response.isEmpty()) {
                return Optional.empty();
            }
            if(response.size() > 1) {
                logger.error("Duplicated user with username {}", userNameOrEmail);
                throw new RuntimeException("Duplicated user with username " + userNameOrEmail);
            }
            return Optional.of(UserId.valueOf(response.get(0).id()));
        } catch (Exception e) {
            logger.error("Error fetching for userID ", e);
            return Optional.empty();
        }
    }
}
