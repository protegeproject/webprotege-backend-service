package edu.stanford.protege.webprotege.perspective;

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
public class ResetPerspectiveLayoutCommandHandler implements CommandHandler<ResetPerspectiveLayoutAction, ResetPerspectiveLayoutResult> {

    private final ActionExecutor executor;

    public ResetPerspectiveLayoutCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return ResetPerspectiveLayoutAction.CHANNEL;
    }

    @Override
    public Class<ResetPerspectiveLayoutAction> getRequestClass() {
        return ResetPerspectiveLayoutAction.class;
    }

    @Override
    public Mono<ResetPerspectiveLayoutResult> handleRequest(ResetPerspectiveLayoutAction request,
                                                            ExecutionContext executionContext) {
        return Mono.just(executor.execute(request, executionContext));
    }
}