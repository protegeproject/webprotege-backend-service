package edu.stanford.protege.webprotege.sharing;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.authorization.ProjectResource;
import edu.stanford.protege.webprotege.authorization.RoleId;
import edu.stanford.protege.webprotege.common.UserId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.protege.webprotege.authorization.Subject.forUser;
import static edu.stanford.protege.webprotege.sharing.PendingSharingInvitation.normalizeKey;

/**
 * Grants and consumes any {@link PendingSharingInvitation}s that match a user the first time they
 * arrive authenticated.  Because there is no login event anywhere in the service, this runs from
 * {@link edu.stanford.protege.webprotege.user.GetAuthenticatedUserDetailsActionHandler} at session
 * bootstrap; idempotency comes from consuming (deleting) each invitation as it is granted.
 * <p>
 * The trust model is deliberately narrow: an email-shaped invitation is only ever redeemed against
 * a matching {@code email} claim whose {@code email_verified} is true - never against the username,
 * even in deployments where the username equals the email address.
 */
public class PendingSharingInvitationRedeemer {

    private static final String EMAIL_CLAIM = "email";

    private static final String EMAIL_VERIFIED_CLAIM = "email_verified";

    private static final String PREFERRED_USERNAME_CLAIM = "preferred_username";

    private static final Logger logger = LoggerFactory.getLogger(PendingSharingInvitationRedeemer.class);

    private final PendingSharingInvitationRepository repository;

    private final AccessManager accessManager;

    private final ObjectMapper objectMapper;

    @Inject
    public PendingSharingInvitationRedeemer(@Nonnull PendingSharingInvitationRepository repository,
                                            @Nonnull AccessManager accessManager,
                                            @Nonnull ObjectMapper objectMapper) {
        this.repository = checkNotNull(repository);
        this.accessManager = checkNotNull(accessManager);
        this.objectMapper = checkNotNull(objectMapper);
    }

    /**
     * Redeems every invitation matching {@code userId} (by lowercase user id) or, for email-shaped
     * invitations, by a matching verified {@code email} claim in {@code jwt}.  Never throws.
     */
    public void redeem(@Nonnull UserId userId, @Nullable String jwt) {
        if (userId.isGuest()) {
            return;
        }
        try {
            String usernameKey = normalizeKey(userId.id());
            JwtEmailClaims claims = parseEmailClaims(jwt, usernameKey);

            Set<String> lookupKeys = new HashSet<>();
            lookupKeys.add(usernameKey);
            if (claims.emailKey() != null) {
                lookupKeys.add(claims.emailKey());
            }

            List<PendingSharingInvitation> matching = new ArrayList<>();
            for (PendingSharingInvitation invitation : repository.findByPersonKeys(lookupKeys)) {
                if (matches(invitation, usernameKey, claims)) {
                    matching.add(invitation);
                }
            }
            // Apply oldest first so that, when several invitations target the same project, the
            // most recently created permission is the one that ends up in effect.
            matching.sort(Comparator.comparing(PendingSharingInvitation::createdAt));
            for (PendingSharingInvitation invitation : matching) {
                redeemOne(userId, invitation);
            }
        } catch (RuntimeException e) {
            logger.error("An unexpected error occurred while redeeming sharing invitations for user {}: {}",
                         userId, e.toString());
        }
    }

    private boolean matches(PendingSharingInvitation invitation, String usernameKey, JwtEmailClaims claims) {
        String key = invitation.personKey();
        if (PendingSharingInvitation.isEmailShaped(key)) {
            if (claims.emailKey() == null || !key.equals(claims.emailKey())) {
                return false;
            }
            if (!claims.emailVerified()) {
                logger.warn("Not redeeming sharing invitation {} - its email matches the authenticated " +
                            "user's email claim but email_verified is not true.", invitation);
                return false;
            }
            return true;
        }
        return key.equals(usernameKey);
    }

    private void redeemOne(UserId userId, PendingSharingInvitation invitation) {
        try {
            // Claim the invitation by deleting it first; a zero deleted-count means another login (or
            // an owner change) already consumed it, so there is nothing to grant.
            if (repository.delete(invitation.projectId(), invitation.personKey()) == 0) {
                return;
            }
            grant(userId, invitation);
        } catch (RuntimeException e) {
            // The invitation has already been claimed and is never re-inserted - a revoked invitation
            // must not resurrect. Log loudly so an owner knows they may need to re-share.
            logger.error("Failed to redeem sharing invitation {} for user {} - it has been consumed and " +
                         "will NOT be restored: {}", invitation, userId, e.toString());
        }
    }

    private void grant(UserId userId, PendingSharingInvitation invitation) {
        ProjectResource projectResource = new ProjectResource(invitation.projectId());
        Collection<RoleId> currentRoles = accessManager.getAssignedRoles(forUser(userId), projectResource);
        Set<RoleId> newRoles = new HashSet<>(currentRoles);
        newRoles.removeAll(Roles.SHARING_ROLE_IDS);
        newRoles.addAll(Roles.fromSharingPermission(invitation.sharingPermission()));
        accessManager.setAssignedRoles(forUser(userId), projectResource, newRoles);
    }

    private JwtEmailClaims parseEmailClaims(@Nullable String jwt, String usernameKey) {
        if (jwt == null) {
            return JwtEmailClaims.absent();
        }
        try {
            String[] segments = jwt.split("\\.");
            if (segments.length < 2) {
                return JwtEmailClaims.absent();
            }
            JsonNode claims = objectMapper.readTree(decodePayload(segments[1]));
            JsonNode emailNode = claims.get(EMAIL_CLAIM);
            if (emailNode == null || emailNode.isNull() || emailNode.asText().isBlank()) {
                return JwtEmailClaims.absent();
            }
            // Defensive subject binding: if the token names a preferred_username that does not match
            // the acting user, ignore its email claim entirely. Today userId and jwt always come from
            // the same ExecutionContext; this protects against a future call site pairing a userId
            // with a mismatched token.
            JsonNode preferredUsername = claims.get(PREFERRED_USERNAME_CLAIM);
            if (preferredUsername != null && !preferredUsername.isNull()) {
                String preferredUsernameKey = normalizeKey(preferredUsername.asText());
                if (!preferredUsernameKey.isEmpty() && !preferredUsernameKey.equals(usernameKey)) {
                    logger.warn("Ignoring the JWT email claim while redeeming sharing invitations: the " +
                                "token's preferred_username '{}' does not match the acting user '{}'.",
                                preferredUsernameKey, usernameKey);
                    return JwtEmailClaims.absent();
                }
            }
            return new JwtEmailClaims(normalizeKey(emailNode.asText()), isVerified(claims.get(EMAIL_VERIFIED_CLAIM)));
        } catch (Exception e) {
            // The token is already authenticated upstream; a payload we cannot parse simply means no
            // usable email claim. Username-keyed invitations are unaffected.
            logger.warn("Could not parse the JWT email claim while redeeming sharing invitations: {}",
                        e.toString());
            return JwtEmailClaims.absent();
        }
    }

    private static byte[] decodePayload(String segment) {
        try {
            return Base64.getUrlDecoder().decode(segment);
        } catch (IllegalArgumentException e) {
            return Base64.getDecoder().decode(segment);
        }
    }

    private static boolean isVerified(@Nullable JsonNode node) {
        if (node == null || node.isNull()) {
            return false;
        }
        if (node.isBoolean()) {
            return node.booleanValue();
        }
        return node.isTextual() && "true".equalsIgnoreCase(node.asText().trim());
    }

    private record JwtEmailClaims(@Nullable String emailKey, boolean emailVerified) {

        static JwtEmailClaims absent() {
            return new JwtEmailClaims(null, false);
        }
    }
}
