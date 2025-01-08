package edu.stanford.protege.webprotege.hierarchy;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

@WebProtegeHandler
public class GetHierarchyDescriptorCommandHandler implements CommandHandler<GetHierarchyDescriptorRequest, GetHierarchyDescriptorResponse> {

    private final Logger logger = LoggerFactory.getLogger(GetHierarchyDescriptorCommandHandler.class);

    private final ActionExecutor actionExecutor;

    public GetHierarchyDescriptorCommandHandler(ActionExecutor actionExecutor) {
        this.actionExecutor = actionExecutor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return GetHierarchyDescriptorRequest.CHANNEL;
    }

    @Override
    public Class<GetHierarchyDescriptorRequest> getRequestClass() {
        return GetHierarchyDescriptorRequest.class;
    }

    @Override
    public Mono<GetHierarchyDescriptorResponse> handleRequest(GetHierarchyDescriptorRequest request, ExecutionContext executionContext) {
        logger.info("GetHierarchyDescriptorCommandHandler::handleRequest: {}", request);
        return actionExecutor.executeRequest(request, executionContext);
    }
}
