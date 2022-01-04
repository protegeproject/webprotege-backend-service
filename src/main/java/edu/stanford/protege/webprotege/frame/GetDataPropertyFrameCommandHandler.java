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
public class GetDataPropertyFrameCommandHandler implements CommandHandler<GetDataPropertyFrameAction, GetDataPropertyFrameResult> {

    private final ActionExecutor executor;

    public GetDataPropertyFrameCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @Nonnull
    @Override
    public String getChannelName() {
        return GetDataPropertyFrameAction.CHANNEL;
    }

    @Override
    public Class<GetDataPropertyFrameAction> getRequestClass() {
        return GetDataPropertyFrameAction.class;
    }

    @Override
    public Mono<GetDataPropertyFrameResult> handleRequest(GetDataPropertyFrameAction request,
                                                          ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}