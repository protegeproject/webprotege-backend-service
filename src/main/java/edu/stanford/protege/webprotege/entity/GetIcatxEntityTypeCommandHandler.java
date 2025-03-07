package edu.stanford.protege.webprotege.entity;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;


@WebProtegeHandler
public class GetIcatxEntityTypeCommandHandler  implements CommandHandler<GetIcatxEntityTypeAction, GetIcatxEntityTypeResult> {

    private final ActionExecutor executor;

    public GetIcatxEntityTypeCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }


    @NotNull
    @Override
    public String getChannelName() {
        return GetIcatxEntityTypeAction.CHANNEL;
    }

    @Override
    public Class<GetIcatxEntityTypeAction> getRequestClass() {
        return GetIcatxEntityTypeAction.class;
    }

    @Override
    public Mono<GetIcatxEntityTypeResult> handleRequest(GetIcatxEntityTypeAction request, ExecutionContext executionContext) {
        return Mono.just(executor.execute(request, executionContext));
    }
}
