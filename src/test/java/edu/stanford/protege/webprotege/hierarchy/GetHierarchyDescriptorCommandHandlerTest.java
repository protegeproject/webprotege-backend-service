package edu.stanford.protege.webprotege.hierarchy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.forms.FormId;
import edu.stanford.protege.webprotege.forms.field.FormRegionId;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.perspective.PerspectiveId;
import edu.stanford.protege.webprotege.ui.DisplayContext;
import edu.stanford.protege.webprotege.ui.ViewId;
import edu.stanford.protege.webprotege.ui.ViewNodeId;
import java.util.List;
import java.util.Map;

import reactor.core.publisher.Mono;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class GetHierarchyDescriptorCommandHandlerTest {

    @Mock
    private ActionExecutor actionExecutor;

    @InjectMocks
    private GetHierarchyDescriptorCommandHandler handler;

    private GetHierarchyDescriptorRequest request;

    private ExecutionContext executionContext;

    private GetHierarchyDescriptorResponse expectedResponse;

    @BeforeEach
    void setUp() {
        var projectId = ProjectId.valueOf("123e4567-e89b-12d3-a456-426614174999");

        var displayContext = new DisplayContext(
                projectId,
                PerspectiveId.get("123e4567-e89b-12d3-a456-426614174001"),
                ViewId.valueOf("view1"),
                ViewNodeId.valueOf("vn1"),
                Map.of("key", "value"),
                List.of(FormId.valueOf("123e4567-e89b-12d3-a456-426614174010")),
                FormRegionId.valueOf("123e4567-e89b-12d3-a456-426614174011"),
                List.of() // empty selectedPaths
        );
        request = new GetHierarchyDescriptorRequest(projectId, displayContext);

        executionContext = new ExecutionContext(UserId.valueOf("user1"), "dummy-jwt-token");

        expectedResponse = new GetHierarchyDescriptorResponse(ClassHierarchyDescriptor.create());
    }

    @Test
    void shouldReturnCorrectChannelName() {
        var channelName = handler.getChannelName();
        assertThat(channelName).isEqualTo(GetHierarchyDescriptorRequest.CHANNEL);
    }

    @Test
    void shouldReturnCorrectRequestClass() {
        var reqClass = handler.getRequestClass();
        assertThat(reqClass).isEqualTo(GetHierarchyDescriptorRequest.class);
    }

    @Test
    void shouldHandleRequestAndReturnExpectedResponse() {
        when(actionExecutor.executeRequest(any(GetHierarchyDescriptorRequest.class), any(ExecutionContext.class)))
                .thenReturn(Mono.just(expectedResponse));

        var resultMono = handler.handleRequest(request, executionContext);

        StepVerifier.create(resultMono)
                .expectNext(expectedResponse)
                .verifyComplete();
    }
}
