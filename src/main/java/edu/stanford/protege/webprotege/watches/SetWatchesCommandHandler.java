package edu.stanford.protege.webprotege.watches;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import javax.annotation.Nonnull;
import reactor.core.publisher.Mono;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-08-21
 */
@WebProtegeHandler
public class SetWatchesCommandHandler implements CommandHandler<SetWatchesAction, SetWatchesResult> {

    private final ActionExecutor executor;

    public SetWatchesCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @Nonnull
    @Override
    public String getChannelName() {
        return SetWatchesAction.CHANNEL;
    }

    @Override
    public Class<SetWatchesAction> getRequestClass() {
        return SetWatchesAction.class;
    }

    @Override
    public Mono<SetWatchesResult> handleRequest(SetWatchesAction request,
                                                      ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}