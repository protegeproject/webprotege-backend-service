package edu.stanford.protege.webprotege.hierarchy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.common.UserId;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class GetHierarchyDescriptorRulesCommandHandlerTest {

    @Mock
    private ActionExecutor actionExecutor;

    @InjectMocks
    private GetHierarchyDescriptorRulesCommandHandler handler;

    private GetProjectHierarchyDescriptorRulesRequest request;

    private ExecutionContext executionContext;

    private GetProjectHierarchyDescriptorRulesResponse expectedResponse;

    @BeforeEach
    void setUp() {
        var projectId = ProjectId.generate();
        request = new GetProjectHierarchyDescriptorRulesRequest(projectId);

        executionContext = new ExecutionContext(UserId.valueOf("123e4567-e89b-12d3-a456-426614174999"), "dummy-jwt-token", "correlationId");

        expectedResponse = new GetProjectHierarchyDescriptorRulesResponse(List.of());
    }

    @Test
    void shouldReturnCorrectChannelName() {
        var channelName = handler.getChannelName();
        assertThat(channelName).isEqualTo(GetHierarchyDescriptorRequest.CHANNEL);
    }

    @Test
    void shouldReturnCorrectRequestClass() {
        var reqClass = handler.getRequestClass();
        assertThat(reqClass).isEqualTo(GetProjectHierarchyDescriptorRulesRequest.class);
    }

    @Test
    void shouldHandleRequestAndReturnExpectedResponse() {
        when(actionExecutor.executeRequest(any(GetProjectHierarchyDescriptorRulesRequest.class), any(ExecutionContext.class)))
                .thenReturn(Mono.just(expectedResponse));

        var resultMono = handler.handleRequest(request, executionContext);

        StepVerifier.create(resultMono)
                .expectNext(expectedResponse)
                .verifyComplete();
    }
}
