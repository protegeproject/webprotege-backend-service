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
public class GetEntityDiscussionThreadsCommandHandler implements CommandHandler<GetEntityDiscussionThreadsAction, GetEntityDiscussionThreadsResult> {

    private final ActionExecutor executor;

    public GetEntityDiscussionThreadsCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return GetEntityDiscussionThreadsAction.CHANNEL;
    }

    @Override
    public Class<GetEntityDiscussionThreadsAction> getRequestClass() {
        return GetEntityDiscussionThreadsAction.class;
    }

    @Override
    public Mono<GetEntityDiscussionThreadsResult> handleRequest(GetEntityDiscussionThreadsAction request,
                                                                ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}