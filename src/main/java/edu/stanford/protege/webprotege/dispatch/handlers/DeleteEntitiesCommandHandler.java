package edu.stanford.protege.webprotege.dispatch.handlers;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.entity.DeleteEntitiesAction;
import edu.stanford.protege.webprotege.entity.DeleteEntitiesResult;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;

@WebProtegeHandler
public class DeleteEntitiesCommandHandler implements CommandHandler<DeleteEntitiesAction, DeleteEntitiesResult> {

    private final ActionExecutor executor;

    public DeleteEntitiesCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @Nonnull
    @Override
    public String getChannelName() {
        return DeleteEntitiesAction.CHANNEL;
    }

    @Override
    public Class<DeleteEntitiesAction> getRequestClass() {
        return DeleteEntitiesAction.class;
    }

    @Override
    public Mono<DeleteEntitiesResult> handleRequest(DeleteEntitiesAction request, ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}
