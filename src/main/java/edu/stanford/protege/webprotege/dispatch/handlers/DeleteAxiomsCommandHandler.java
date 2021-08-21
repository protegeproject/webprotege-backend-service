package edu.stanford.protege.webprotege.dispatch.handlers;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.dispatch.actions.DeleteAxiomsAction;
import edu.stanford.protege.webprotege.dispatch.actions.DeleteAxiomsResult;
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
public class DeleteAxiomsCommandHandler implements CommandHandler<DeleteAxiomsAction, DeleteAxiomsResult> {

    private final ActionExecutor executor;

    public DeleteAxiomsCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return DeleteAxiomsAction.CHANNEL;
    }

    @Override
    public Class<DeleteAxiomsAction> getRequestClass() {
        return DeleteAxiomsAction.class;
    }

    @Override
    public Mono<DeleteAxiomsResult> handleRequest(DeleteAxiomsAction request, ExecutionContext executionContext) {
        return Mono.just(executor.execute(request, executionContext));
    }
}