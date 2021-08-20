package edu.stanford.protege.webprotege.renderer;

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
public class GetEntityRenderingCommandHandler implements CommandHandler<GetEntityRenderingAction, GetEntityRenderingResult> {

    private final ActionExecutor executor;

    public GetEntityRenderingCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return GetEntityRenderingAction.CHANNEL;
    }

    @Override
    public Class<GetEntityRenderingAction> getRequestClass() {
        return GetEntityRenderingAction.class;
    }

    @Override
    public Mono<GetEntityRenderingResult> handleRequest(GetEntityRenderingAction request,
                                                        ExecutionContext executionContext) {
        return Mono.just(executor.execute(request, executionContext));
    }
}