package edu.stanford.protege.webprotege.icd.actions;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.*;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

@WebProtegeHandler
public class GetEntityChildrenCommandHandler implements CommandHandler<GetEntityChildrenAction, GetEntityChildrenResult> {
    private final ActionExecutor executor;

    public GetEntityChildrenCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return GetEntityChildrenAction.CHANNEL;
    }

    @Override
    public Class<GetEntityChildrenAction> getRequestClass() {
        return GetEntityChildrenAction.class;
    }

    @Override
    public Mono<GetEntityChildrenResult> handleRequest(GetEntityChildrenAction request, ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}
