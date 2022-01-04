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
public class GetObjectPropertyFrameCommandHandler implements CommandHandler<GetObjectPropertyFrameAction, GetObjectPropertyFrameResult> {

    private final ActionExecutor executor;

    public GetObjectPropertyFrameCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @Nonnull
    @Override
    public String getChannelName() {
        return GetObjectPropertyFrameAction.CHANNEL;
    }

    @Override
    public Class<GetObjectPropertyFrameAction> getRequestClass() {
        return GetObjectPropertyFrameAction.class;
    }

    @Override
    public Mono<GetObjectPropertyFrameResult> handleRequest(GetObjectPropertyFrameAction request,
                                                            ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}