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
 * 2021-08-21
 */
@WebProtegeHandler
public class SetOntologyAnnotationsCommandHandler implements CommandHandler<SetOntologyAnnotationsAction, SetOntologyAnnotationsResult> {

    private final ActionExecutor executor;

    public SetOntologyAnnotationsCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return SetOntologyAnnotationsAction.CHANNEL;
    }

    @Override
    public Class<SetOntologyAnnotationsAction> getRequestClass() {
        return SetOntologyAnnotationsAction.class;
    }

    @Override
    public Mono<SetOntologyAnnotationsResult> handleRequest(SetOntologyAnnotationsAction request,
                                                            ExecutionContext executionContext) {
        return Mono.just(executor.execute(request, executionContext));
    }
}