package edu.stanford.protege.webprotege.entity;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;


@WebProtegeHandler
public class GetRenderedOwlEntitiesCommandHandler implements CommandHandler<GetRenderedOwlEntitiesAction, GetRenderedOwlEntitiesResult> {

    private final ActionExecutor executor;

    public GetRenderedOwlEntitiesCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return GetRenderedOwlEntitiesAction.CHANNEL;
    }

    @Override
    public Class<GetRenderedOwlEntitiesAction> getRequestClass() {
        return GetRenderedOwlEntitiesAction.class;
    }

    @Override
    public Mono<GetRenderedOwlEntitiesResult> handleRequest(GetRenderedOwlEntitiesAction request, ExecutionContext executionContext) {
        return Mono.just(executor.execute(request, executionContext));
    }
}
