package edu.stanford.protege.webprotege.watches;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-08-19
 */
@WebProtegeHandler
public class GetEntityWatchesCommandHandler implements CommandHandler<GetWatchesAction, GetWatchesResult> {

    private final ActionExecutor executor;

    public GetEntityWatchesCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return GetWatchesAction.CHANNEL;
    }

    @Override
    public Class<GetWatchesAction> getRequestClass() {
        return GetWatchesAction.class;
    }

    @Override
    public Mono<GetWatchesResult> handleRequest(GetWatchesAction request, ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}
