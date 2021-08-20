package edu.stanford.protege.webprotege.user;

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
public class GetUserIdCompletionsCommandHandler implements CommandHandler<GetUserIdCompletionsAction, GetUserIdCompletionsResult> {

    private final ActionExecutor executor;

    public GetUserIdCompletionsCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return GetUserIdCompletionsAction.CHANNEL;
    }

    @Override
    public Class<GetUserIdCompletionsAction> getRequestClass() {
        return GetUserIdCompletionsAction.class;
    }

    @Override
    public Mono<GetUserIdCompletionsResult> handleRequest(GetUserIdCompletionsAction request,
                                                          ExecutionContext executionContext) {
        return Mono.just(executor.execute(request, executionContext));
    }
}