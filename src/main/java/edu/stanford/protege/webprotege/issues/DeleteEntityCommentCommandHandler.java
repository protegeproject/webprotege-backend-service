package edu.stanford.protege.webprotege.issues;

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
public class DeleteEntityCommentCommandHandler implements CommandHandler<DeleteEntityCommentAction, DeleteEntityCommentResult> {

    private final ActionExecutor executor;

    public DeleteEntityCommentCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return DeleteEntityCommentAction.CHANNEL;
    }

    @Override
    public Class<DeleteEntityCommentAction> getRequestClass() {
        return DeleteEntityCommentAction.class;
    }

    @Override
    public Mono<DeleteEntityCommentResult> handleRequest(DeleteEntityCommentAction request,
                                                         ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}