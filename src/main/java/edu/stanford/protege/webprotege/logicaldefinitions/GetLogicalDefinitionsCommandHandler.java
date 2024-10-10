package edu.stanford.protege.webprotege.logicaldefinitions;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

public class GetLogicalDefinitionsCommandHandler implements CommandHandler<GetLogicalDefinitionsRequest, GetLogicalDefinitionsResponse >{

    private final ActionExecutor executor;

    public GetLogicalDefinitionsCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return GetLogicalDefinitionsRequest.CHANNEL;
    }

    @Override
    public Class<GetLogicalDefinitionsRequest> getRequestClass() {
        return GetLogicalDefinitionsRequest.class;
    }

    @Override
    public Mono<GetLogicalDefinitionsResponse> handleRequest(GetLogicalDefinitionsRequest request, ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}
