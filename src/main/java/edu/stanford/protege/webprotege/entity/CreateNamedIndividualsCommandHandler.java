package edu.stanford.protege.webprotege.entity;

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
public class CreateNamedIndividualsCommandHandler implements CommandHandler<CreateNamedIndividualsAction, CreateNamedIndividualsResult> {

    private final ActionExecutor executor;

    public CreateNamedIndividualsCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return CreateNamedIndividualsAction.CHANNEL;
    }

    @Override
    public Class<CreateNamedIndividualsAction> getRequestClass() {
        return CreateNamedIndividualsAction.class;
    }

    @Override
    public Mono<CreateNamedIndividualsResult> handleRequest(CreateNamedIndividualsAction request,
                                                            ExecutionContext executionContext) {
        return Mono.just(executor.execute(request, executionContext));
    }
}