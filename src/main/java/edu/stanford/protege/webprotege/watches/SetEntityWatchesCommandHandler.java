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
 * 2021-08-21
 */
@WebProtegeHandler
public class SetEntityWatchesCommandHandler implements CommandHandler<SetEntityWatchesAction, SetEntityWatchesResult> {

    private final ActionExecutor executor;

    public SetEntityWatchesCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return SetEntityWatchesAction.CHANNEL;
    }

    @Override
    public Class<SetEntityWatchesAction> getRequestClass() {
        return SetEntityWatchesAction.class;
    }

    @Override
    public Mono<SetEntityWatchesResult> handleRequest(SetEntityWatchesAction request,
                                                      ExecutionContext executionContext) {
        return Mono.just(executor.execute(request, executionContext));
    }
}