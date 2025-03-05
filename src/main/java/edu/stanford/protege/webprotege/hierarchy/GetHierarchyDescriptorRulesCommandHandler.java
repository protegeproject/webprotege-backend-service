package edu.stanford.protege.webprotege.hierarchy;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

@WebProtegeHandler
public class GetHierarchyDescriptorRulesCommandHandler implements CommandHandler<GetProjectHierarchyDescriptorRulesRequest, GetProjectHierarchyDescriptorRulesResponse> {

    private final ActionExecutor actionExecutor;

    public GetHierarchyDescriptorRulesCommandHandler(ActionExecutor actionExecutor) {
        this.actionExecutor = actionExecutor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return GetHierarchyDescriptorRequest.CHANNEL;
    }

    @Override
    public Class<GetProjectHierarchyDescriptorRulesRequest> getRequestClass() {
        return GetProjectHierarchyDescriptorRulesRequest.class;
    }

    @Override
    public Mono<GetProjectHierarchyDescriptorRulesResponse> handleRequest(GetProjectHierarchyDescriptorRulesRequest request, ExecutionContext executionContext) {
        return actionExecutor.executeRequest(request, executionContext);
    }
}
