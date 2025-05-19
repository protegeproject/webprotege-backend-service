package edu.stanford.protege.webprotege.entity;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

@WebProtegeHandler
public class GetMatchingCriteriaCommandHandler  implements CommandHandler<GetMatchingCriteriaAction, GetMatchingCriteriaResult> {

    private final ActionExecutor executor;

    public GetMatchingCriteriaCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }


    @NotNull
    @Override
    public String getChannelName() {
        return GetMatchingCriteriaAction.CHANNEL;
    }

    @Override
    public Class<GetMatchingCriteriaAction> getRequestClass() {
        return GetMatchingCriteriaAction.class;
    }

    @Override
    public Mono<GetMatchingCriteriaResult> handleRequest(GetMatchingCriteriaAction request, ExecutionContext executionContext) {
        return Mono.just(executor.execute(request, executionContext));
    }
}
