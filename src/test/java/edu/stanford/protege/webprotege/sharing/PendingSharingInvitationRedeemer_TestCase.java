package edu.stanford.protege.webprotege.sharing;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.protege.webprotege.MockingUtils;
import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.authorization.ProjectResource;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;

import static edu.stanford.protege.webprotege.authorization.Subject.forUser;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * Covers {@link PendingSharingInvitationRedeemer}.  Locks in the deliberately narrow trust model
 * (email-shaped keys only ever redeem against a matching, verified email claim - never the
 * username), the JWT payload decoding edge cases, createdAt ordering and the fault-isolation /
 * never-throws guarantees.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PendingSharingInvitationRedeemer_TestCase {

    private static final String HEADER = base64Url("{\"alg\":\"none\"}");

    @Mock
    private PendingSharingInvitationRepository repository;

    @Mock
    private AccessManager accessManager;

    private PendingSharingInvitationRedeemer redeemer;

    private final ProjectId projectId = MockingUtils.mockProjectId();

    private final ProjectResource projectResource = new ProjectResource(projectId);

    private final UserId userId = UserId.valueOf("jdoe");

    @BeforeEach
    public void setUp() {
        redeemer = new PendingSharingInvitationRedeemer(repository, accessManager, new ObjectMapper());
    }

    private PendingSharingInvitation invitation(String key, SharingPermission permission) {
        return invitation(projectId, key, permission, Instant.now());
    }

    private PendingSharingInvitation invitation(ProjectId projectId,
                                                String key,
                                                SharingPermission permission,
                                                Instant createdAt) {
        return new PendingSharingInvitation(projectId, key, key, permission, MockingUtils.mockUserId(), createdAt);
    }

    @Test
    public void shouldGrantAndConsumeUsernameKeyedInvitation() {
        PendingSharingInvitation invitation = invitation("jdoe", SharingPermission.VIEW);
        when(repository.findByPersonKeys(any())).thenReturn(List.of(invitation));
        when(repository.delete(projectId, "jdoe")).thenReturn(1L);

        redeemer.redeem(userId, jwt("{}"));

        verify(repository).delete(projectId, "jdoe");
        verify(accessManager).setAssignedRoles(eq(forUser(userId)), eq(projectResource),
                                               eq(new HashSet<>(Roles.fromSharingPermission(SharingPermission.VIEW))));
    }

    @Test
    public void shouldGrantEmailKeyedInvitationWhenEmailClaimMatchesAndIsVerified() {
        PendingSharingInvitation invitation = invitation("new@x.org", SharingPermission.MANAGE);
        when(repository.findByPersonKeys(any())).thenReturn(List.of(invitation));
        when(repository.delete(projectId, "new@x.org")).thenReturn(1L);

        redeemer.redeem(userId, jwt("{\"email\":\"New@x.org\",\"email_verified\":true}"));

        verify(repository).delete(projectId, "new@x.org");
        verify(accessManager).setAssignedRoles(eq(forUser(userId)), eq(projectResource),
                                               eq(new HashSet<>(Roles.fromSharingPermission(SharingPermission.MANAGE))));
    }

    @Test
    public void shouldGrantEmailKeyedInvitationWhenEmailVerifiedIsStringTrue() {
        PendingSharingInvitation invitation = invitation("new@x.org", SharingPermission.EDIT);
        when(repository.findByPersonKeys(any())).thenReturn(List.of(invitation));
        when(repository.delete(projectId, "new@x.org")).thenReturn(1L);

        redeemer.redeem(userId, jwt("{\"email\":\"new@x.org\",\"email_verified\":\"true\"}"));

        verify(repository).delete(projectId, "new@x.org");
        verify(accessManager).setAssignedRoles(eq(forUser(userId)), eq(projectResource), any());
    }

    @Test
    public void shouldNotRedeemEmailKeyedInvitationWhenEmailVerifiedIsFalse() {
        PendingSharingInvitation invitation = invitation("new@x.org", SharingPermission.MANAGE);
        when(repository.findByPersonKeys(any())).thenReturn(List.of(invitation));

        redeemer.redeem(userId, jwt("{\"email\":\"new@x.org\",\"email_verified\":false}"));

        verify(repository, never()).delete(any(), anyString());
        verify(accessManager, never()).setAssignedRoles(any(), any(), any());
    }

    @Test
    public void shouldNotRedeemEmailKeyedInvitationWhenEmailVerifiedClaimIsAbsent() {
        PendingSharingInvitation invitation = invitation("new@x.org", SharingPermission.MANAGE);
        when(repository.findByPersonKeys(any())).thenReturn(List.of(invitation));

        redeemer.redeem(userId, jwt("{\"email\":\"new@x.org\"}"));

        verify(repository, never()).delete(any(), anyString());
        verify(accessManager, never()).setAssignedRoles(any(), any(), any());
    }

    @Test
    public void shouldNeverRedeemEmailShapedKeyViaTheUsernamePath() {
        // Deployment reality: registrationEmailAsUsername=true, so the username can equal the email.
        // An email-shaped invitation must still never be redeemed by the username alone.
        UserId emailUsername = UserId.valueOf("new@x.org");
        PendingSharingInvitation invitation = invitation("new@x.org", SharingPermission.MANAGE);
        when(repository.findByPersonKeys(any())).thenReturn(List.of(invitation));

        redeemer.redeem(emailUsername, jwt("{}"));

        verify(repository, never()).delete(any(), anyString());
        verify(accessManager, never()).setAssignedRoles(any(), any(), any());
    }

    @Test
    public void shouldApplyMatchingInvitationsInAscendingCreatedAtOrder() {
        Instant earlier = Instant.parse("2024-01-01T00:00:00Z");
        Instant later = Instant.parse("2024-06-01T00:00:00Z");
        // Same project, two different keys that both match this user; the newer one must be applied
        // last so its permission is the one that ends up in effect.
        PendingSharingInvitation olderByUsername = invitation(projectId, "jdoe", SharingPermission.VIEW, earlier);
        PendingSharingInvitation newerByEmail = invitation(projectId, "new@x.org", SharingPermission.MANAGE, later);
        when(repository.findByPersonKeys(any())).thenReturn(List.of(newerByEmail, olderByUsername));
        when(repository.delete(eq(projectId), anyString())).thenReturn(1L);

        redeemer.redeem(userId, jwt("{\"email\":\"new@x.org\",\"email_verified\":true}"));

        InOrder inOrder = inOrder(accessManager);
        inOrder.verify(accessManager).setAssignedRoles(eq(forUser(userId)), eq(projectResource),
                                                       eq(new HashSet<>(Roles.fromSharingPermission(SharingPermission.VIEW))));
        inOrder.verify(accessManager).setAssignedRoles(eq(forUser(userId)), eq(projectResource),
                                                       eq(new HashSet<>(Roles.fromSharingPermission(SharingPermission.MANAGE))));
    }

    @Test
    public void shouldIgnoreEmailClaimWhenPreferredUsernameDoesNotMatchTheActingUser() {
        PendingSharingInvitation invitation = invitation("new@x.org", SharingPermission.MANAGE);
        when(repository.findByPersonKeys(any())).thenReturn(List.of(invitation));

        // The token names a preferred_username other than the acting userId, so its email claim is
        // ignored and the email-shaped invitation is not redeemed.
        redeemer.redeem(userId,
                        jwt("{\"email\":\"new@x.org\",\"email_verified\":true,\"preferred_username\":\"someone-else\"}"));

        verify(repository, never()).delete(any(), anyString());
        verify(accessManager, never()).setAssignedRoles(any(), any(), any());
    }

    @Test
    public void shouldRedeemWhenPreferredUsernameMatchesTheActingUser() {
        PendingSharingInvitation invitation = invitation("new@x.org", SharingPermission.MANAGE);
        when(repository.findByPersonKeys(any())).thenReturn(List.of(invitation));
        when(repository.delete(projectId, "new@x.org")).thenReturn(1L);

        redeemer.redeem(userId,
                        jwt("{\"email\":\"new@x.org\",\"email_verified\":true,\"preferred_username\":\"jdoe\"}"));

        verify(repository).delete(projectId, "new@x.org");
    }

    @Test
    public void shouldBeANoOpForTheGuestUser() {
        redeemer.redeem(UserId.getGuest(), jwt("{\"email\":\"new@x.org\",\"email_verified\":true}"));

        verifyNoInteractions(repository, accessManager);
    }

    @Test
    public void shouldRedeemWhenPayloadUsesPaddedUrlSafeBase64() {
        PendingSharingInvitation invitation = invitation("new@x.org", SharingPermission.EDIT);
        when(repository.findByPersonKeys(any())).thenReturn(List.of(invitation));
        when(repository.delete(projectId, "new@x.org")).thenReturn(1L);

        String payload = Base64.getUrlEncoder()
                               .encodeToString("{\"email\":\"new@x.org\",\"email_verified\":true}"
                                                       .getBytes(StandardCharsets.UTF_8));
        redeemer.redeem(userId, HEADER + "." + payload + ".sig");

        verify(repository).delete(projectId, "new@x.org");
    }

    @Test
    public void shouldRedeemWhenPayloadUsesStandardBase64() {
        PendingSharingInvitation invitation = invitation("new@x.org", SharingPermission.EDIT);
        when(repository.findByPersonKeys(any())).thenReturn(List.of(invitation));
        when(repository.delete(projectId, "new@x.org")).thenReturn(1L);

        redeemer.redeem(userId, standardBase64Jwt());

        verify(repository).delete(projectId, "new@x.org");
    }

    @Test
    public void shouldSwallowUnparseableJwtButStillRedeemUsernameKeyedInvitation() {
        PendingSharingInvitation invitation = invitation("jdoe", SharingPermission.VIEW);
        when(repository.findByPersonKeys(any())).thenReturn(List.of(invitation));
        when(repository.delete(projectId, "jdoe")).thenReturn(1L);

        assertDoesNotThrow(() -> redeemer.redeem(userId, "not-a-valid-jwt"));

        verify(repository).delete(projectId, "jdoe");
        verify(accessManager).setAssignedRoles(eq(forUser(userId)), eq(projectResource), any());
    }

    @Test
    public void shouldNotGrantWhenTheInvitationWasAlreadyClaimed() {
        PendingSharingInvitation invitation = invitation("jdoe", SharingPermission.VIEW);
        when(repository.findByPersonKeys(any())).thenReturn(List.of(invitation));
        // Another login (or an owner change) already consumed the invitation.
        when(repository.delete(projectId, "jdoe")).thenReturn(0L);

        redeemer.redeem(userId, jwt("{}"));

        verify(accessManager, never()).setAssignedRoles(any(), any(), any());
    }

    @Test
    public void shouldNotThrowNorReInsertWhenAGrantFails() {
        PendingSharingInvitation invitation = invitation("jdoe", SharingPermission.VIEW);
        when(repository.findByPersonKeys(any())).thenReturn(List.of(invitation));
        when(repository.delete(projectId, "jdoe")).thenReturn(1L);
        doThrow(new RuntimeException("grant failed")).when(accessManager)
                                                     .setAssignedRoles(eq(forUser(userId)), eq(projectResource), any());

        assertDoesNotThrow(() -> redeemer.redeem(userId, jwt("{}")));

        // A consumed invitation is never restored, even when the grant fails.
        verify(repository, never()).upsert(any());
    }

    @Test
    public void shouldIsolateAFailedInvitationFromTheRemainingOnes() {
        ProjectId otherProjectId = MockingUtils.mockProjectId();
        PendingSharingInvitation failing = invitation(projectId, "jdoe", SharingPermission.VIEW, Instant.parse("2024-01-01T00:00:00Z"));
        PendingSharingInvitation succeeding = invitation(otherProjectId, "jdoe", SharingPermission.EDIT, Instant.parse("2024-02-01T00:00:00Z"));
        when(repository.findByPersonKeys(any())).thenReturn(List.of(failing, succeeding));
        when(repository.delete(eq(projectId), eq("jdoe"))).thenReturn(1L);
        when(repository.delete(eq(otherProjectId), eq("jdoe"))).thenReturn(1L);
        doThrow(new RuntimeException("grant failed")).when(accessManager)
                                                     .setAssignedRoles(eq(forUser(userId)), eq(projectResource), any());

        redeemer.redeem(userId, jwt("{}"));

        // The failure on the first invitation must not prevent the second from being granted.
        verify(accessManager).setAssignedRoles(eq(forUser(userId)), eq(new ProjectResource(otherProjectId)), any());
    }

    private static String jwt(String payloadJson) {
        return HEADER + "." + base64Url(payloadJson) + ".sig";
    }

    private static String base64Url(String value) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Builds a JWT whose payload is encoded with the standard (non-URL-safe) Base64 alphabet, growing
     * a filler claim until the encoded payload actually contains a '+' or '/' - characters the
     * URL-safe decoder rejects, so the redeemer must fall back to the standard decoder.
     */
    private static String standardBase64Jwt() {
        for (int i = 0; i < 256; i++) {
            String json = "{\"email\":\"new@x.org\",\"email_verified\":true,\"pad\":\""
                    + "ÿ".repeat(i) + "\"}";
            String payload = Base64.getEncoder().encodeToString(json.getBytes(StandardCharsets.UTF_8));
            if (payload.contains("+") || payload.contains("/")) {
                return HEADER + "." + payload + ".sig";
            }
        }
        throw new IllegalStateException("Could not build a standard-base64 payload containing '+' or '/'");
    }
}
