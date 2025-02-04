package edu.stanford.protege.webprotege.hierarchy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.authorization.ActionId;
import edu.stanford.protege.webprotege.authorization.ProjectResource;
import edu.stanford.protege.webprotege.authorization.Subject;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.forms.FormId;
import edu.stanford.protege.webprotege.forms.field.FormRegionId;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.perspective.PerspectiveId;
import edu.stanford.protege.webprotege.ui.DisplayContext;
import edu.stanford.protege.webprotege.ui.ViewId;
import edu.stanford.protege.webprotege.ui.ViewNodeId;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetHierarchyDescriptorActionHandlerTest {

    @Mock
    private AccessManager accessManager;

    @Mock
    private HierarchyDescriptorRuleSelector ruleSelector;

    @InjectMocks
    private GetHierarchyDescriptorActionHandler handler;

    private GetHierarchyDescriptorRequest request;

    private ExecutionContext executionContext;

    @BeforeEach
    void setUp() {
        var projectId = ProjectId.valueOf("123e4567-e89b-12d3-a456-426614174999");

        var displayContext = new DisplayContext(
                projectId,
                PerspectiveId.get("123e4567-e89b-12d3-a456-426614174001"),
                ViewId.valueOf("view1"),
                ViewNodeId.valueOf("vn1"),
                java.util.Map.of("key", "value"),
                List.of(FormId.valueOf("123e4567-e89b-12d3-a456-426614174010")),
                FormRegionId.valueOf("123e4567-e89b-12d3-a456-426614174011"),
                List.of()  // empty selectedPaths
        );
        request = new GetHierarchyDescriptorRequest(projectId, displayContext);

        executionContext = new ExecutionContext(UserId.valueOf("user1"), "dummy-jwt-token");
    }

    @Test
    void shouldReturnCorrectActionClass() {
        assertThat(handler.getActionClass()).isEqualTo(GetHierarchyDescriptorRequest.class);
    }

    @Test
    void shouldReturnViewProjectBuiltInAction() {
        var action = handler.getRequiredExecutableBuiltInAction(request);
        assertThat(action).isEqualTo(BuiltInAction.VIEW_PROJECT);
    }

    @Test
    void shouldExecuteAndReturnMatchingHierarchyDescriptor() {
        var actionClosure = Set.of(ActionId.valueOf("ACTIONX"), ActionId.valueOf("ACTIONY"));
        when(accessManager.getActionClosure(any(Subject.class), any(ProjectResource.class), any(ExecutionContext.class)))
                .thenReturn(actionClosure);

        var dummyRule = HierarchyDescriptorRule.create(ClassHierarchyDescriptor.create());
        when(ruleSelector.selectRule(request.projectId(), request.displayContext(), actionClosure))
                .thenReturn(Optional.of(dummyRule));

        var response = handler.execute(request, executionContext);
        assertThat(response.hierarchyDescriptor()).isEqualTo(dummyRule.hierarchyDescriptor());
    }

    @Test
    void shouldExecuteAndReturnNullWhenNoMatchingRule() {
        var actionClosure = Set.of(ActionId.valueOf("ACTIONX"), ActionId.valueOf("ACTIONY"));
        when(accessManager.getActionClosure(any(Subject.class), any(ProjectResource.class), any(ExecutionContext.class)))
                .thenReturn(actionClosure);

        when(ruleSelector.selectRule(request.projectId(), request.displayContext(), actionClosure))
                .thenReturn(Optional.empty());

        var response = handler.execute(request, executionContext);
        assertThat(response.hierarchyDescriptor()).isNull();
    }
}
