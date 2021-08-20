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
public class AddEntityCommentCommandHandler implements CommandHandler<AddEntityCommentAction, AddEntityCommentResult> {

    private final ActionExecutor executor;

    public AddEntityCommentCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return AddEntityCommentAction.CHANNEL;
    }

    @Override
    public Class<AddEntityCommentAction> getRequestClass() {
        return AddEntityCommentAction.class;
    }

    @Override
    public Mono<AddEntityCommentResult> handleRequest(AddEntityCommentAction request,
                                                      ExecutionContext executionContext) {
        return Mono.just(executor.execute(request, executionContext));
    }
}