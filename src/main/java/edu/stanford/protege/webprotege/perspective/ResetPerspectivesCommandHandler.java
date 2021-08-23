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
public class ResetPerspectivesCommandHandler implements CommandHandler<ResetPerspectivesAction, ResetPerspectivesResult> {

    private final ActionExecutor executor;

    public ResetPerspectivesCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return ResetPerspectivesAction.CHANNEL;
    }

    @Override
    public Class<ResetPerspectivesAction> getRequestClass() {
        return ResetPerspectivesAction.class;
    }

    @Override
    public Mono<ResetPerspectivesResult> handleRequest(ResetPerspectivesAction request,
                                                       ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}