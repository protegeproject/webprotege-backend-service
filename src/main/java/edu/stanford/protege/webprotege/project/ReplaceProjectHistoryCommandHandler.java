package edu.stanford.protege.webprotege.project;

import edu.stanford.protege.webprotege.access.BuiltInCapability;
import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.authorization.Capability;
import edu.stanford.protege.webprotege.authorization.ProjectResource;
import edu.stanford.protege.webprotege.authorization.Resource;
import edu.stanford.protege.webprotege.ipc.AuthorizedCommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;

@WebProtegeHandler
public class ReplaceProjectHistoryCommandHandler implements AuthorizedCommandHandler<ReplaceProjectHistoryRequest, ReplaceProjectHistoryResponse> {

    private static final Logger logger = LoggerFactory.getLogger(ReplaceProjectHistoryCommandHandler.class);

    private final ActionExecutor actionExecutor;

    public ReplaceProjectHistoryCommandHandler(ActionExecutor actionExecutor) {
        this.actionExecutor = actionExecutor;
    }

    @NotNull
    @Override
    public Resource getTargetResource(ReplaceProjectHistoryRequest request) {
        return ProjectResource.forProject(request.projectId());
    }

    @NotNull
    @Override
    public Collection<Capability> getRequiredCapabilities() {
        return List.of(BuiltInCapability.REPLACE_PROJECT_HISTORY.getCapability());
    }

    @NotNull
    @Override
    public String getChannelName() {
        return ReplaceProjectHistoryRequest.CHANNEL;
    }

    @Override
    public Class<ReplaceProjectHistoryRequest> getRequestClass() {
        return ReplaceProjectHistoryRequest.class;
    }

    @Override
    public Mono<ReplaceProjectHistoryResponse> handleRequest(ReplaceProjectHistoryRequest request, ExecutionContext executionContext) {
        logger.info("[{}] Received request to replace entire project history", request.projectId());
        return actionExecutor.executeRequest(request, executionContext);
    }
}
