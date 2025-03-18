package edu.stanford.protege.webprotege.entity;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.*;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;


@WebProtegeHandler
public class GetEntityDirectParentsCommandHandler implements CommandHandler<GetEntityDirectParentsAction, GetEntityDirectParentsResult> {

    private final ActionExecutor executor;

    public GetEntityDirectParentsCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @Nonnull
    @Override
    public String getChannelName() {
        return GetEntityDirectParentsAction.CHANNEL;
    }

    @Override
    public Class<GetEntityDirectParentsAction> getRequestClass() {
        return GetEntityDirectParentsAction.class;
    }

    @Override
    public Mono<GetEntityDirectParentsResult> handleRequest(GetEntityDirectParentsAction request,
                                                           ExecutionContext executionContext) {
        return Mono.just(executor.execute(request, executionContext));
    }
}