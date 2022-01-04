package edu.stanford.protege.webprotege.issues;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import javax.annotation.Nonnull;
import reactor.core.publisher.Mono;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-08-20
 */
@WebProtegeHandler
public class DeleteEntityCommentCommandHandler implements CommandHandler<DeleteCommentAction, DeleteCommentResult> {

    private final ActionExecutor executor;

    public DeleteEntityCommentCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @Nonnull
    @Override
    public String getChannelName() {
        return DeleteCommentAction.CHANNEL;
    }

    @Override
    public Class<DeleteCommentAction> getRequestClass() {
        return DeleteCommentAction.class;
    }

    @Override
    public Mono<DeleteCommentResult> handleRequest(DeleteCommentAction request,
                                                         ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}