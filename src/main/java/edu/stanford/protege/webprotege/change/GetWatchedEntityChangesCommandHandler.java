package edu.stanford.protege.webprotege.change;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-08-21
 */
@WebProtegeHandler
public class GetWatchedEntityChangesCommandHandler implements CommandHandler<GetWatchedEntityChangesAction, GetWatchedEntityChangesResult> {

    private final ActionExecutor executor;

    public GetWatchedEntityChangesCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return GetWatchedEntityChangesAction.CHANNEL;
    }

    @Override
    public Class<GetWatchedEntityChangesAction> getRequestClass() {
        return GetWatchedEntityChangesAction.class;
    }

    @Override
    public Mono<GetWatchedEntityChangesResult> handleRequest(GetWatchedEntityChangesAction request,
                                                             ExecutionContext executionContext) {
        return Mono.just(executor.execute(request, executionContext));
    }
}