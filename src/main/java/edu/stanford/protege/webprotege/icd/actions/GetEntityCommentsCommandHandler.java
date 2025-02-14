package edu.stanford.protege.webprotege.icd.actions;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.*;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

@WebProtegeHandler
public class GetEntityCommentsCommandHandler implements CommandHandler<GetEntityCommentsAction, GetEntityCommentsResponse> {

    private final ActionExecutor executor;

    public GetEntityCommentsCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }


    @NotNull
    @Override
    public String getChannelName() {
        return GetEntityCommentsAction.CHANNEL;
    }

    @Override
    public Class<GetEntityCommentsAction> getRequestClass() {
        return GetEntityCommentsAction.class;
    }

    @Override
    public Mono<GetEntityCommentsResponse> handleRequest(GetEntityCommentsAction request, ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}
