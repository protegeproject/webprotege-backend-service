package edu.stanford.protege.webprotege.entity;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import javax.annotation.Nonnull;
import reactor.core.publisher.Mono;

@WebProtegeHandler
public class CheckNonExistentIrisCommandHandler
        implements CommandHandler<CheckNonExistentIrisAction, CheckNonExistentIrisResult> {

    private final ActionExecutor executor;

    public CheckNonExistentIrisCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @Nonnull
    @Override
    public String getChannelName() {
        return CheckNonExistentIrisAction.CHANNEL;
    }

    @Override
    public Class<CheckNonExistentIrisAction> getRequestClass() {
        return CheckNonExistentIrisAction.class;
    }

    @Override
    public Mono<CheckNonExistentIrisResult> handleRequest(
            CheckNonExistentIrisAction request,
            ExecutionContext executionContext
    ) {
        return executor.executeRequest(request, executionContext);
    }
}
