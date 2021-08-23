package edu.stanford.protege.webprotege.viz;

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
public class GetEntityGraphCommandHandler implements CommandHandler<GetEntityGraphAction, GetEntityGraphResult> {

    private final ActionExecutor executor;

    public GetEntityGraphCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return GetEntityGraphAction.CHANNEL;
    }

    @Override
    public Class<GetEntityGraphAction> getRequestClass() {
        return GetEntityGraphAction.class;
    }

    @Override
    public Mono<GetEntityGraphResult> handleRequest(GetEntityGraphAction request, ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}