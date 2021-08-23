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
public class GetEntityHtmlRenderingCommandHandler implements CommandHandler<GetEntityHtmlRenderingAction, GetEntityHtmlRenderingResult> {

    private final ActionExecutor executor;

    public GetEntityHtmlRenderingCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return GetEntityHtmlRenderingAction.CHANNEL;
    }

    @Override
    public Class<GetEntityHtmlRenderingAction> getRequestClass() {
        return GetEntityHtmlRenderingAction.class;
    }

    @Override
    public Mono<GetEntityHtmlRenderingResult> handleRequest(GetEntityHtmlRenderingAction request,
                                                            ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}