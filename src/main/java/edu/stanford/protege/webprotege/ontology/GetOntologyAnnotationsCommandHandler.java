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
public class GetOntologyAnnotationsCommandHandler implements CommandHandler<GetOntologyAnnotationsAction, GetOntologyAnnotationsResult> {

    private final ActionExecutor executor;

    public GetOntologyAnnotationsCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return GetOntologyAnnotationsAction.CHANNEL;
    }

    @Override
    public Class<GetOntologyAnnotationsAction> getRequestClass() {
        return GetOntologyAnnotationsAction.class;
    }

    @Override
    public Mono<GetOntologyAnnotationsResult> handleRequest(GetOntologyAnnotationsAction request,
                                                            ExecutionContext executionContext) {
        return Mono.just(executor.execute(request, executionContext));
    }
}