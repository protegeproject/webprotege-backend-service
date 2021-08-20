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
public class GetIndividualsPageContainingIndividualCommandHandler implements CommandHandler<GetIndividualsPageContainingIndividualAction, GetIndividualsPageContainingIndividualResult> {

    private final ActionExecutor executor;

    public GetIndividualsPageContainingIndividualCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return GetIndividualsPageContainingIndividualAction.CHANNEL;
    }

    @Override
    public Class<GetIndividualsPageContainingIndividualAction> getRequestClass() {
        return GetIndividualsPageContainingIndividualAction.class;
    }

    @Override
    public Mono<GetIndividualsPageContainingIndividualResult> handleRequest(GetIndividualsPageContainingIndividualAction request,
                                                                            ExecutionContext executionContext) {
        return Mono.just(executor.execute(request, executionContext));
    }
}