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
public class CreateEntityDiscussionThreadCommandHandler implements CommandHandler<CreateEntityDiscussionThreadAction, CreateEntityDiscussionThreadResult> {

    private final ActionExecutor executor;

    public CreateEntityDiscussionThreadCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return CreateEntityDiscussionThreadAction.CHANNEL;
    }

    @Override
    public Class<CreateEntityDiscussionThreadAction> getRequestClass() {
        return CreateEntityDiscussionThreadAction.class;
    }

    @Override
    public Mono<CreateEntityDiscussionThreadResult> handleRequest(CreateEntityDiscussionThreadAction request,
                                                                  ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}