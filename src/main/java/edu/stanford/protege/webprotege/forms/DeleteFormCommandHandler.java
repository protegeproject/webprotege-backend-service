package edu.stanford.protege.webprotege.forms;

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
public class DeleteFormCommandHandler implements CommandHandler<DeleteFormAction, DeleteFormResult> {

    private final ActionExecutor executor;

    public DeleteFormCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return DeleteFormAction.CHANNEL;
    }

    @Override
    public Class<DeleteFormAction> getRequestClass() {
        return DeleteFormAction.class;
    }

    @Override
    public Mono<DeleteFormResult> handleRequest(DeleteFormAction request, ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}