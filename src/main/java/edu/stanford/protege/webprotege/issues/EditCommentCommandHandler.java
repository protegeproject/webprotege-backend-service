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
public class EditCommentCommandHandler implements CommandHandler<UpdateCommentAction, UpdateCommentResult> {

    private final ActionExecutor executor;

    public EditCommentCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return UpdateCommentAction.CHANNEL;
    }

    @Override
    public Class<UpdateCommentAction> getRequestClass() {
        return UpdateCommentAction.class;
    }

    @Override
    public Mono<UpdateCommentResult> handleRequest(UpdateCommentAction request, ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}