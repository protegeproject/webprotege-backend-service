package edu.stanford.protege.webprotege.logicaldefinitions;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

@WebProtegeHandler
public class GetLogicalDefinitionsCommandHandler implements CommandHandler<GetLogicalDefinitionsAction, GetLogicalDefinitionsResponse >{

    private final ActionExecutor executor;

    public GetLogicalDefinitionsCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return GetLogicalDefinitionsAction.CHANNEL;
    }

    @Override
    public Class<GetLogicalDefinitionsAction> getRequestClass() {
        return GetLogicalDefinitionsAction.class;
    }

    @Override
    public Mono<GetLogicalDefinitionsResponse> handleRequest(GetLogicalDefinitionsAction request, ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}
