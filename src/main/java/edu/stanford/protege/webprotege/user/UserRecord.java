package edu.stanford.protege.webprotege.user;

import com.google.common.base.Objects;
import edu.stanford.protege.webprotege.common.UserId;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 09/03/16
 *
 * Represents the information about a user that is stored in a DB.  This is not a data structure for
 * general purpose use.
 */
public class UserRecord {

    @Nonnull
    private final UserId userId;

    @Nonnull
    private final String realName;

    @Nonnull
    private final String emailAddress;

    @Nonnull
    private final String avatarUrl;

    /**
     * @param userId The userId that identifies this record in the DB
     * @param realName The real name of the user.  May be empty, but must not be {@code null}.
     * @param emailAddress The email address of the user.   May be empty, but must not be {@code null}.
     * @param avatarUrl The Avatar URL of the user.    May be empty, but must not be {@code null}.
     */
    public UserRecord(@Nonnull UserId userId,
                      @Nonnull String realName,
                      @Nonnull String emailAddress,
                      @Nonnull String avatarUrl) {
        this.userId = checkNotNull(userId);
        this.realName = checkNotNull(realName);
        this.emailAddress = checkNotNull(emailAddress);
        this.avatarUrl = checkNotNull(avatarUrl);
    }

    /**
     * Gets the user Id.
     * @return The user Id.  Not {@code null}.
     */
    @Nonnull
    public UserId getUserId() {
        return userId;
    }

    @Nonnull
    public String getRealName() {
        return realName;
    }

    /**
     * Gets the email address.
     * @return The email address.  May be empty to indicate no specified address.  Not {@code null}.
     */
    @Nonnull
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Gets the avatar Url.
     * @return The avatar Url.  May be empty to indicate no specified Url.  Not {@code null}.
     */
    @Nonnull
    public String getAvatarUrl() {
        return avatarUrl;
    }



    @Override
    public String toString() {
        return toStringHelper("UserRecord")
                .addValue(userId)
                .addValue(realName)
                .addValue(emailAddress)
                .addValue(avatarUrl)
                .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(
                userId,
                realName,
                emailAddress,
                avatarUrl
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof UserRecord other)) {
            return false;
        }
        return this.userId.equals(other.userId)
                && this.realName.equals(other.realName)
                && this.emailAddress.equals(other.emailAddress)
                && this.avatarUrl.equals(other.avatarUrl);
    }
}
