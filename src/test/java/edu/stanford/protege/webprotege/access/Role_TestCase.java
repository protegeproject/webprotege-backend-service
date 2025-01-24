
package edu.stanford.protege.webprotege.access;

import edu.stanford.protege.webprotege.authorization.ActionId;
import edu.stanford.protege.webprotege.authorization.RoleId;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class Role_TestCase {

    private Role role;

    private RoleId roleId = new RoleId("Role"), parentRoleId = new RoleId("ParentRole");

    private ActionId actionId = new ActionId("SomeAction");

    private List<RoleId> parents;

    private List<ActionId> actions;

    @BeforeEach
    public void setUp() {
        parents = singletonList(parentRoleId);
        actions = singletonList(actionId);
        role = new Role(roleId, parents, actions);
    }

    @SuppressWarnings("ConstantConditions")
    @Test
public void shouldThrowNullPointerExceptionIf_roleId_IsNull() {
    assertThrows(NullPointerException.class, () -> { 
        new Role(null, parents, actions);
     });
}

    @Test
    public void shouldReturnSupplied_roleId() {
        assertThat(role.getRoleId(), is(this.roleId));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
public void shouldThrowNullPointerExceptionIf_parents_IsNull() {
    assertThrows(NullPointerException.class, () -> { 
        new Role(roleId, null, actions);
     });
}

    @Test
    public void shouldReturnSupplied_parents() {
        assertThat(role.getParents(), is(this.parents));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
public void shouldThrowNullPointerExceptionIf_actions_IsNull() {
    assertThrows(NullPointerException.class, () -> { 
        new Role(roleId, parents, null);
     });
}

    @Test
    public void shouldReturnSupplied_actions() {
        assertThat(role.getActions(), is(this.actions));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(role, is(role));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(role.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(role, is(new Role(roleId, parents, actions)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_roleId() {
        assertThat(role, is(not(new Role(new RoleId("AnotherRole"), parents, actions))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_parents() {
        assertThat(role, is(not(new Role(roleId, singletonList(new RoleId("OtherRole")), actions))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_actions() {
        assertThat(role, is(not(new Role(roleId, parents, singletonList(new ActionId("AnotherAction"))))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(role.hashCode(), is(new Role(roleId, parents, actions).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(role.toString(), Matchers.startsWith("Role"));
    }

}
