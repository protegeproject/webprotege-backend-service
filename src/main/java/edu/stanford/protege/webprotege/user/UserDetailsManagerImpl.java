package edu.stanford.protege.webprotege.user;

import edu.stanford.protege.webprotege.common.UserId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 06/02/15
 */
public class UserDetailsManagerImpl implements UserDetailsManager {

    private final UserRecordRepository repository;

    private final Logger logger = LoggerFactory.getLogger(UserDetailsManagerImpl.class);

    @Inject
    public UserDetailsManagerImpl(UserRecordRepository userRecordRepository) {
        this.repository = checkNotNull(userRecordRepository);
    }

    @Override
    public List<UserId> getUserIdsContainingIgnoreCase(String userName, int limit) {
        return repository.findByUserIdContainingIgnoreCase(userName, limit);
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
        if (!record.isPresent()) {
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
        if (!record.isPresent()) {
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
        Optional<UserRecord> byUserId = repository.findOne(UserId.valueOf(userNameOrEmail));
        if (byUserId.isPresent()) {
            return Optional.of(byUserId.get().getUserId());
        }
        Optional<UserRecord> byEmail = repository.findOneByEmailAddress(userNameOrEmail);
        return byEmail.map(UserRecord::getUserId);
    }
}
