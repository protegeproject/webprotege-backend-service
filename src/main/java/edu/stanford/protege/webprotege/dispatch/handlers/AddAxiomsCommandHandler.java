package edu.stanford.protege.webprotege.dispatch.handlers;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.dispatch.actions.AddAxiomsAction;
import edu.stanford.protege.webprotege.dispatch.actions.AddAxiomsResult;
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
public class AddAxiomsCommandHandler implements CommandHandler<AddAxiomsAction, AddAxiomsResult> {

    private final ActionExecutor executor;

    public AddAxiomsCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return AddAxiomsAction.CHANNEL;
    }

    @Override
    public Class<AddAxiomsAction> getRequestClass() {
        return AddAxiomsAction.class;
    }

    @Override
    public Mono<AddAxiomsResult> handleRequest(AddAxiomsAction request, ExecutionContext executionContext) {
        return Mono.just(executor.execute(request, executionContext));
    }
}