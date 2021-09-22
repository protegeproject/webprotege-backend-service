package edu.stanford.protege.webprotege.user;

import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.persistence.*;
import org.bson.Document;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30 Sep 2016
 */
public class UserRecordConverter implements DocumentConverter<UserRecord> {

    private static final String USER_ID = "_id";

    private static final String REAL_NAME = "realName";

    private static final String EMAIL_ADDRESS = "emailAddress";

    private static final String AVATAR_URL = "avatar";

    private static final String SALT = "salt";

    private static final String SALTED_PASSWORD_DIGEST = "saltedPasswordDigest";

    @Inject
    public UserRecordConverter() {
    }

    @Override
    public Document toDocument(@Nonnull UserRecord object) {
        Document document = new Document();
        document.append(USER_ID, object.getUserId().id());
        document.append(REAL_NAME, object.getRealName());
        document.append(EMAIL_ADDRESS, object.getEmailAddress());
        if (!object.getAvatarUrl().isEmpty()) {
            document.append(AVATAR_URL, object.getAvatarUrl());
        }
        return document;
    }

    @Override
    public UserRecord fromDocument(@Nonnull Document document) {
        String userId = document.getString(USER_ID);
        String realName = document.getString(REAL_NAME);
        String email = orEmptyString(document.getString(EMAIL_ADDRESS));
        String avatar = orEmptyString(document.getString(AVATAR_URL));
        return new UserRecord(
                UserId.valueOf(userId),
                realName,
                email,
                avatar);
    }

    public UserId getUserId(Document document) {
        return UserId.valueOf(document.getString(USER_ID));
    }

    @Nonnull
    private static String orEmptyString(@Nullable String s) {
        if (s == null) {
            return "";
        }
        else {
            return s;
        }
    }

    public static Document byUserId(@Nonnull UserId userId) {
        return new Document(USER_ID, userId.id());
    }

    public static Document byEmailAddress(@Nonnull String emailAddress) {
        return new Document(EMAIL_ADDRESS, emailAddress);
    }

    public static Document byUserIdContainsIgnoreCase(@Nonnull String regex) {
        return new Document(USER_ID, new Document("$regex" , regex).append("$options" , "i" ));
    }

    public static Document withUserId() {
        return new Document(USER_ID, 1);
    }

}
