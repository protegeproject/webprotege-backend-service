package edu.stanford.protege.webprotege.bulkop;

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
public class SetAnnotationValueCommandHandler implements CommandHandler<SetAnnotationValueAction, SetAnnotationValueResult> {

    private final ActionExecutor executor;

    public SetAnnotationValueCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return SetAnnotationValueAction.CHANNEL;
    }

    @Override
    public Class<SetAnnotationValueAction> getRequestClass() {
        return SetAnnotationValueAction.class;
    }

    @Override
    public Mono<SetAnnotationValueResult> handleRequest(SetAnnotationValueAction request,
                                                        ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}