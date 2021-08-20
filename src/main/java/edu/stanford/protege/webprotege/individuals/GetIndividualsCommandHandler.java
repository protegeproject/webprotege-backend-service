package edu.stanford.protege.webprotege.individuals;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-08-20
 */
@WebProtegeHandler
public class GetIndividualsCommandHandler implements CommandHandler<GetIndividualsAction, GetIndividualsResult> {

    private final ActionExecutor executor;

    public GetIndividualsCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return GetIndividualsAction.CHANNEL;
    }

    @Override
    public Class<GetIndividualsAction> getRequestClass() {
        return GetIndividualsAction.class;
    }

    @Override
    public Mono<GetIndividualsResult> handleRequest(GetIndividualsAction request, ExecutionContext executionContext) {
        return Mono.just(executor.execute(request, executionContext));
    }
}