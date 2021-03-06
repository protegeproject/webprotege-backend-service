package edu.stanford.protege.webprotege.frame;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import javax.annotation.Nonnull;
import reactor.core.publisher.Mono;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-08-20
 */
@WebProtegeHandler
public class GetClassFrameCommandHandler implements CommandHandler<GetClassFrameAction, GetClassFrameResult> {

    private final ActionExecutor executor;

    public GetClassFrameCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @Nonnull
    @Override
    public String getChannelName() {
        return GetClassFrameAction.CHANNEL;
    }

    @Override
    public Class<GetClassFrameAction> getRequestClass() {
        return GetClassFrameAction.class;
    }

    @Override
    public Mono<GetClassFrameResult> handleRequest(GetClassFrameAction request, ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}