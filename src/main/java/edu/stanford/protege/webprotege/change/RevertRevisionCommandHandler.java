package edu.stanford.protege.webprotege.change;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import javax.annotation.Nonnull;
import reactor.core.publisher.Mono;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-08-21
 */
@WebProtegeHandler
public class RevertRevisionCommandHandler implements CommandHandler<RevertRevisionAction, RevertRevisionResult> {

    private final ActionExecutor executor;

    public RevertRevisionCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @Nonnull
    @Override
    public String getChannelName() {
        return RevertRevisionAction.CHANNEL;
    }

    @Override
    public Class<RevertRevisionAction> getRequestClass() {
        return RevertRevisionAction.class;
    }

    @Override
    public Mono<RevertRevisionResult> handleRequest(RevertRevisionAction request, ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}