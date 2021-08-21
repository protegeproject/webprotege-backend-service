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
public class UpdateAnnotationPropertyFrameCommandHandler implements CommandHandler<UpdateAnnotationPropertyFrameAction, UpdateAnnotationPropertyFrameResult> {

    private final ActionExecutor executor;

    public UpdateAnnotationPropertyFrameCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return UpdateAnnotationPropertyFrameAction.CHANNEL;
    }

    @Override
    public Class<UpdateAnnotationPropertyFrameAction> getRequestClass() {
        return UpdateAnnotationPropertyFrameAction.class;
    }

    @Override
    public Mono<UpdateAnnotationPropertyFrameResult> handleRequest(UpdateAnnotationPropertyFrameAction request,
                                                                   ExecutionContext executionContext) {
        return Mono.just(executor.execute(request, executionContext));
    }
}