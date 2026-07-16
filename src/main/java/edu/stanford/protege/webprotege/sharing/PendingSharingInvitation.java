package edu.stanford.protege.webprotege.sharing;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;

import java.time.Instant;
import java.util.Locale;
import java.util.Objects;

/**
 * A sharing invitation for a person who could not be resolved to an existing user when a project
 * was shared with them.  It records both the id exactly as an owner typed it ({@link #personId})
 * and a normalized match key ({@link #personKey}) so that the invitation can be matched against a
 * user's id or verified email claim when they first authenticate.
 */
public record PendingSharingInvitation(
        @JsonProperty(PROJECT_ID) ProjectId projectId,
        @JsonProperty(PERSON_ID) String personId,
        @JsonProperty(PERSON_KEY) String personKey,
        @JsonProperty(SHARING_PERMISSION) SharingPermission sharingPermission,
        @JsonProperty(INVITED_BY) UserId invitedBy,
        @JsonProperty(CREATED_AT) Instant createdAt) {

    public static final String PROJECT_ID = "projectId";

    public static final String PERSON_ID = "personId";

    public static final String PERSON_KEY = "personKey";

    public static final String SHARING_PERMISSION = "sharingPermission";

    public static final String INVITED_BY = "invitedBy";

    public static final String CREATED_AT = "createdAt";

    public PendingSharingInvitation {
        // Reject a null in any component up front so a malformed stored document fails conversion
        // (which the repository's reads treat as a corrupt-document signal) rather than surfacing as
        // a later NullPointerException in PersonId.get / sort / fromSharingPermission.
        Objects.requireNonNull(projectId, "projectId must not be null");
        Objects.requireNonNull(personId, "personId must not be null");
        Objects.requireNonNull(personKey, "personKey must not be null");
        Objects.requireNonNull(sharingPermission, "sharingPermission must not be null");
        Objects.requireNonNull(invitedBy, "invitedBy must not be null");
        Objects.requireNonNull(createdAt, "createdAt must not be null");
        personId = personId.trim();
        personKey = normalizeKey(personKey);
        if (personKey.isEmpty()) {
            throw new IllegalArgumentException("A pending sharing invitation must have a non-blank person key");
        }
    }

    /**
     * Normalizes a typed person id into a match key by trimming surrounding whitespace and
     * lower-casing it.  A {@code null} value normalizes to the empty string.  Shared by the code
     * that writes invitations and the code that matches them on login so both agree on what makes
     * two ids "the same person".
     */
    public static String normalizeKey(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ENGLISH);
    }

    /**
     * @return whether {@code key} looks like an email address (i.e. contains an {@code '@'}).
     * Applied to a normalized key so the manager's "send an email invitation" gate and the
     * redeemer's "match only via a verified email claim" path can never disagree on what counts as
     * email-shaped.
     */
    public static boolean isEmailShaped(String key) {
        return key != null && key.contains("@");
    }
}
