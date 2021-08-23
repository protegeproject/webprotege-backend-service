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
 * 2021-08-20
 */
@WebProtegeHandler
public class GetAnnotationPropertyFrameCommandHandler implements CommandHandler<GetAnnotationPropertyFrameAction, GetAnnotationPropertyFrameResult> {

    private final ActionExecutor executor;

    public GetAnnotationPropertyFrameCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return GetAnnotationPropertyFrameAction.CHANNEL;
    }

    @Override
    public Class<GetAnnotationPropertyFrameAction> getRequestClass() {
        return GetAnnotationPropertyFrameAction.class;
    }

    @Override
    public Mono<GetAnnotationPropertyFrameResult> handleRequest(GetAnnotationPropertyFrameAction request,
                                                                ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}