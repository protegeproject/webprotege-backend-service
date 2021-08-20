package edu.stanford.protege.webprotege.ontology;

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
public class GetRootOntologyIdCommandHandler implements CommandHandler<GetRootOntologyIdAction, GetRootOntologyIdResult> {

    private final ActionExecutor executor;

    public GetRootOntologyIdCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return GetRootOntologyIdAction.CHANNEL;
    }

    @Override
    public Class<GetRootOntologyIdAction> getRequestClass() {
        return GetRootOntologyIdAction.class;
    }

    @Override
    public Mono<GetRootOntologyIdResult> handleRequest(GetRootOntologyIdAction request,
                                                       ExecutionContext executionContext) {
        return Mono.just(executor.execute(request, executionContext));
    }
}