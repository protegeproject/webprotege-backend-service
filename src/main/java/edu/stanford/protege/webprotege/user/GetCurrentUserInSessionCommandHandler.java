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
public class GetCurrentUserInSessionCommandHandler implements CommandHandler<GetCurrentUserInSessionAction, GetCurrentUserInSessionResult> {

    private final ActionExecutor executor;

    public GetCurrentUserInSessionCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return GetCurrentUserInSessionAction.CHANNEL;
    }

    @Override
    public Class<GetCurrentUserInSessionAction> getRequestClass() {
        return GetCurrentUserInSessionAction.class;
    }

    @Override
    public Mono<GetCurrentUserInSessionResult> handleRequest(GetCurrentUserInSessionAction request,
                                                             ExecutionContext executionContext) {
        return Mono.just(executor.execute(request, executionContext));
    }
}