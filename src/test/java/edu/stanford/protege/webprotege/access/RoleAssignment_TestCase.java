
package edu.stanford.protege.webprotege.access;

import edu.stanford.protege.webprotege.authorization.RoleId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class RoleAssignment_TestCase {

    private RoleAssignment roleAssignment;

    private String userName = "The userName";

    private String projectId = "The projectId";

    private RoleId assignedRoleId = new RoleId("ChildRole"), parentRoleId = new RoleId("ParentRole") ;

    private List<String> assignedRoles;

    private List<String> roleClosure;

    private List<String> actionClosure;

    @BeforeEach
    public void setUp() {
        assignedRoles = singletonList("AssignedRole");
        roleClosure = asList("AssignedRole", "ParentRole");
        actionClosure = asList("ActionA", "ActionB");
        roleAssignment = new RoleAssignment(userName, projectId, assignedRoles, roleClosure, actionClosure);
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void shouldNotThrowNullPointerExceptionIf_userName_IsNull() {
        new RoleAssignment(null, projectId, assignedRoles, roleClosure, actionClosure);
    }

    @Test
    public void shouldReturnSupplied_userName() {
        assertThat(roleAssignment.getUserName(), is(Optional.of(this.userName)));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void shouldNotThrowNullPointerExceptionIf_projectId_IsNull() {
        new RoleAssignment(userName, null, assignedRoles, roleClosure, actionClosure);
    }

    @Test
    public void shouldReturnSupplied_projectId() {
        assertThat(roleAssignment.getProjectId(), is(Optional.of(this.projectId)));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
public void shouldThrowNullPointerExceptionIf_assignedRoles_IsNull() {
    assertThrows(NullPointerException.class, () -> { 
        new RoleAssignment(userName, projectId, null, roleClosure, actionClosure);
     });
}

    @Test
    public void shouldReturnSupplied_assignedRoles() {
        assertThat(roleAssignment.getAssignedRoles(), is(this.assignedRoles));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
public void shouldThrowNullPointerExceptionIf_roleClosure_IsNull() {
    assertThrows(NullPointerException.class, () -> { 
        new RoleAssignment(userName, projectId, assignedRoles, null, actionClosure);
     });
}

    @Test
    public void shouldReturnSupplied_roleClosure() {
        assertThat(roleAssignment.getRoleClosure(), is(this.roleClosure));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
public void shouldThrowNullPointerExceptionIf_actionClosure_IsNull() {
    assertThrows(NullPointerException.class, () -> { 
        new RoleAssignment(userName, projectId, assignedRoles, roleClosure, null);
     });
}

    @Test
    public void shouldReturnSupplied_actionClosure() {
        assertThat(roleAssignment.getActionClosure(), is(this.actionClosure));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(roleAssignment, is(roleAssignment));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(roleAssignment.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(roleAssignment, is(new RoleAssignment(userName, projectId, assignedRoles, roleClosure, actionClosure)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_userName() {
        assertThat(roleAssignment, is(not(new RoleAssignment("String-0f264c16-d94e-48f1-a75a-1fa3f65f9a54", projectId, assignedRoles, roleClosure, actionClosure))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        assertThat(roleAssignment, is(not(new RoleAssignment(userName, "String-edc4cdc7-faf7-49e8-a139-78fe82a3babb", assignedRoles, roleClosure, actionClosure))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_assignedRoles() {
        assertThat(roleAssignment, is(not(new RoleAssignment(userName, projectId, singletonList("OtherAssignedRole"), roleClosure, actionClosure))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_roleClosure() {
        assertThat(roleAssignment, is(not(new RoleAssignment(userName, projectId, assignedRoles, singletonList("OtherRoleClosure"), actionClosure))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_actionClosure() {
        assertThat(roleAssignment, is(not(new RoleAssignment(userName, projectId, assignedRoles, roleClosure, singletonList("OtherActionClosure")))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(roleAssignment.hashCode(), is(new RoleAssignment(userName, projectId, assignedRoles, roleClosure, actionClosure).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(roleAssignment.toString(), startsWith("RoleAssignment"));
    }

}
