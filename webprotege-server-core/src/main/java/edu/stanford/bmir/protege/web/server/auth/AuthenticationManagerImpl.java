package edu.stanford.bmir.protege.web.server.auth;

import edu.stanford.bmir.protege.web.server.user.UserRecord;
import edu.stanford.bmir.protege.web.server.user.UserRecordRepository;
import edu.stanford.bmir.protege.web.shared.auth.*;
import edu.stanford.bmir.protege.web.shared.user.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
public class AuthenticationManagerImpl implements AuthenticationManager {

    private final UserRecordRepository repository;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationManagerImpl.class);

    private final PasswordDigestAlgorithm passwordDigestAlgorithm;

    private final SaltProvider saltProvider;

    @Inject
    public AuthenticationManagerImpl(UserRecordRepository repository,
                                     PasswordDigestAlgorithm passwordDigestAlgorithm,
                                     SaltProvider saltProvider) {
        this.repository = repository;
        this.passwordDigestAlgorithm = passwordDigestAlgorithm;
        this.saltProvider = saltProvider;
    }

    @Override
    public UserDetails registerUser(UserId userId, EmailAddress email, Password password) throws UserRegistrationException {
        checkNotNull(userId);
        checkNotNull(email);
        checkNotNull(password);
        Optional<UserRecord> existingRecord = repository.findOne(userId);
        if(existingRecord.isPresent()) {
            throw new UserNameAlreadyExistsException(userId.getUserName());
        }
        Optional<UserRecord> existingRecordByEmail = repository.findOneByEmailAddress(email.getEmailAddress());
        if(existingRecordByEmail.isPresent()) {
            throw new UserEmailAlreadyExistsException(email.getEmailAddress());
        }
        logger.info("Created new user account for {} with email address {}", userId, email.getEmailAddress());
        var salt = saltProvider.get();
        var digestedPassword = passwordDigestAlgorithm.getDigestOfSaltedPassword(password.getPassword(),
                                                                                 salt);
        var newUserRecord = new UserRecord(
                userId,
                userId.getUserName(),
                email.getEmailAddress(),
                "",
                salt,
                digestedPassword
        );
        repository.save(newUserRecord);
        return UserDetails.getUserDetails(userId, userId.getUserName(), email.getEmailAddress());
    }

    private void setDigestedPassword(UserId userId, SaltedPasswordDigest saltedPasswordDigest, Salt salt) {
        if (userId.isGuest()) {
            return;
        }
        Optional<UserRecord> record = repository.findOne(userId);
        if (record.isEmpty()) {
            return;
        }
        UserRecord replacementRecord = new UserRecord(
                record.get().getUserId(),
                record.get().getRealName(),
                record.get().getEmailAddress(),
                record.get().getAvatarUrl(),
                salt,
                saltedPasswordDigest
        );
        repository.save(replacementRecord);
    }

    @Override
    public AuthenticationResponse authenticateUser(@Nonnull UserId userId, @Nonnull Password password) {
        Optional<UserRecord> existingRecord = repository.findOne(userId);
        return existingRecord.map(r -> checkPassword(password, r))
                             .filter(authenticated -> authenticated)
                             .map(authenticated -> AuthenticationResponse.SUCCESS)
                             .orElse(AuthenticationResponse.FAIL);
    }

    @Override
    public void setPassword(@Nonnull UserId userId, @Nonnull Password newPassword) {
        var salt = saltProvider.get();
        var digestedPassword = passwordDigestAlgorithm.getDigestOfSaltedPassword(newPassword.getPassword(), salt);
        setDigestedPassword(userId, digestedPassword, salt);
    }

    @Nonnull
    private Boolean checkPassword(@Nonnull Password password, UserRecord userRecord) {
        var salt = userRecord.getSalt();
        var digest = passwordDigestAlgorithm.getDigestOfSaltedPassword(password.getPassword(), salt);
        return userRecord.getSaltedPasswordDigest().equals(digest);
    }
}
