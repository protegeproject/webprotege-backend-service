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
public class UpdateClassFrameCommandHandler implements CommandHandler<UpdateClassFrameAction, UpdateClassFrameResult> {

    private final ActionExecutor executor;

    public UpdateClassFrameCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return UpdateClassFrameAction.CHANNEL;
    }

    @Override
    public Class<UpdateClassFrameAction> getRequestClass() {
        return UpdateClassFrameAction.class;
    }

    @Override
    public Mono<UpdateClassFrameResult> handleRequest(UpdateClassFrameAction request,
                                                      ExecutionContext executionContext) {
        return Mono.just(executor.execute(request, executionContext));
    }
}