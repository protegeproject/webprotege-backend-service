package edu.stanford.protege.webprotege.access;

import edu.stanford.protege.webprotege.authorization.ApplicationResource;
import edu.stanford.protege.webprotege.authorization.RoleId;
import edu.stanford.protege.webprotege.authorization.Subject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Apr 2017
 */
@SuppressWarnings("unchecked")
@SpringBootTest
public class AccessManagerImpl_IT {

    private static final String THE_USER_NAME = "The User";

    private static final String USER_NAME_FIELD = "userName";

    private static final String ASSIGNED_ROLES_FIELD = "assignedRoles";

    private static final String ROLE_CLOSURE_FIELD = "roleClosure";

    private static final String ACTION_CLOSURE_FIELD = "actionClosure";

    @Autowired
    private AccessManagerImpl manager;

    private Subject subject = Subject.forUser("JaneDoe");

    private ApplicationResource resource = ApplicationResource.get();

    private Set<RoleId> assignedRoles = Set.of(new RoleId("RoleA"), new RoleId("RoleB"));


    @BeforeEach
    public void setUp() throws Exception {
    }

    @Test
    void shouldSetAssignedRoles() {
        manager.setAssignedRoles(subject, resource, assignedRoles);
        var setRoles = manager.getAssignedRoles(subject, resource);
        assertThat(setRoles).isEqualTo(assignedRoles);
    }
}
