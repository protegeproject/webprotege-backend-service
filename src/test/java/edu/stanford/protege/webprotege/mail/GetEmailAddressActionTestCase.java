package edu.stanford.protege.webprotege.mail;

import edu.stanford.protege.webprotege.user.UserId;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 06/11/2013
 */
public class GetEmailAddressActionTestCase {

    @Test(expected = NullPointerException.class)
    public void nullUserIdThrowsNullPointerException() {
        GetEmailAddressAction.create(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void guestUserIdThrowsIllegalArgumentException() {
        GetEmailAddressAction.create(UserId.getGuest());
    }

    @Test
    public void getUserIdReturnsSuppliedUserId() {
        UserId userId = UserId.getUserId("UserA");
        GetEmailAddressAction action = GetEmailAddressAction.create(userId);
        assertEquals(userId, action.getUserId());
    }

    @Test
    public void equalsReturnsTrueForSameUserId() {
        UserId userId = UserId.getUserId("UserB");
        GetEmailAddressAction actionA = GetEmailAddressAction.create(userId);
        GetEmailAddressAction actionB = GetEmailAddressAction.create(userId);
        assertEquals(actionA, actionB);
    }

    @Test
    public void hashCodeReturnsSameValueForSameUserId() {
        UserId userId = UserId.getUserId("UserC");
        GetEmailAddressAction actionA = GetEmailAddressAction.create(userId);
        GetEmailAddressAction actionB = GetEmailAddressAction.create(userId);
        assertEquals(actionA.hashCode(), actionB.hashCode());
    }
}
