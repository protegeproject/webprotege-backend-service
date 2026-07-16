package edu.stanford.protege.webprotege.sharing;

import edu.stanford.protege.webprotege.MockingUtils;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PendingSharingInvitation_TestCase {

    private final ProjectId projectId = MockingUtils.mockProjectId();

    private final UserId invitedBy = MockingUtils.mockUserId();

    private final Instant createdAt = Instant.parse("2024-01-01T00:00:00Z");

    private PendingSharingInvitation invitation(String personId, String personKey) {
        return new PendingSharingInvitation(projectId, personId, personKey, SharingPermission.VIEW, invitedBy, createdAt);
    }

    @Test
    public void shouldTrimTheAsTypedPersonId() {
        assertThat(invitation("  new@x.org  ", "new@x.org").personId(), is("new@x.org"));
    }

    @Test
    public void shouldNormalizeThePersonKey() {
        assertThat(invitation("New@X.org", "  New@X.org  ").personKey(), is("new@x.org"));
    }

    @Test
    public void shouldRejectABlankPersonKey() {
        assertThrows(IllegalArgumentException.class, () -> invitation("   ", "   "));
    }

    @Test
    public void shouldRejectANullPersonKey() {
        assertThrows(NullPointerException.class, () -> invitation("x", null));
    }

    @Test
    public void shouldRejectANullProjectId() {
        assertThrows(NullPointerException.class,
                     () -> new PendingSharingInvitation(null, "x", "x", SharingPermission.VIEW, invitedBy, createdAt));
    }

    @Test
    public void shouldRejectANullPersonId() {
        assertThrows(NullPointerException.class,
                     () -> new PendingSharingInvitation(projectId, null, "x", SharingPermission.VIEW, invitedBy, createdAt));
    }

    @Test
    public void shouldRejectANullSharingPermission() {
        assertThrows(NullPointerException.class,
                     () -> new PendingSharingInvitation(projectId, "x", "x", null, invitedBy, createdAt));
    }

    @Test
    public void shouldRejectANullInvitedBy() {
        assertThrows(NullPointerException.class,
                     () -> new PendingSharingInvitation(projectId, "x", "x", SharingPermission.VIEW, null, createdAt));
    }

    @Test
    public void shouldRejectANullCreatedAt() {
        assertThrows(NullPointerException.class,
                     () -> new PendingSharingInvitation(projectId, "x", "x", SharingPermission.VIEW, invitedBy, null));
    }

    @Test
    public void normalizeKeyShouldTrimAndLowercase() {
        assertThat(PendingSharingInvitation.normalizeKey("  New@X.org  "), is("new@x.org"));
    }

    @Test
    public void normalizeKeyShouldMapNullToEmptyString() {
        assertThat(PendingSharingInvitation.normalizeKey(null), is(""));
    }

    @Test
    public void isEmailShapedShouldAgreeWithContainsAtOnNormalizedKeys() {
        // Both the manager's email gate and the redeemer's match path delegate to this predicate, so
        // pin its behaviour: email-shapedness is exactly "the normalized key contains an '@'".
        assertThat(PendingSharingInvitation.isEmailShaped("new@x.org"), is(true));
        assertThat(PendingSharingInvitation.isEmailShaped("jdoe"), is(false));
        assertThat(PendingSharingInvitation.isEmailShaped(""), is(false));
        assertThat(PendingSharingInvitation.isEmailShaped(null), is(false));
    }
}
