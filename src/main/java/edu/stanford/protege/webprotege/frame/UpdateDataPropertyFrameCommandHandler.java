package edu.stanford.protege.webprotege.frame;

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
public class UpdateDataPropertyFrameCommandHandler implements CommandHandler<UpdateDataPropertyFrameAction, UpdateDataPropertyFrameResult> {

    private final ActionExecutor executor;

    public UpdateDataPropertyFrameCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return UpdateDataPropertyFrameAction.CHANNEL;
    }

    @Override
    public Class<UpdateDataPropertyFrameAction> getRequestClass() {
        return UpdateDataPropertyFrameAction.class;
    }

    @Override
    public Mono<UpdateDataPropertyFrameResult> handleRequest(UpdateDataPropertyFrameAction request,
                                                             ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}