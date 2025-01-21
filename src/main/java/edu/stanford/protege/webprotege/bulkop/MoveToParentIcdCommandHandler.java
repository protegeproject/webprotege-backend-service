package edu.stanford.protege.webprotege.bulkop;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;


@WebProtegeHandler
public class MoveToParentIcdCommandHandler implements CommandHandler<MoveEntitiesToParentIcdAction, MoveEntitiesToParentIcdResult> {

    private final ActionExecutor executor;

    public MoveToParentIcdCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @Nonnull
    @Override
    public String getChannelName() {
        return MoveEntitiesToParentIcdAction.CHANNEL;
    }

    @Override
    public Class<MoveEntitiesToParentIcdAction> getRequestClass() {
        return MoveEntitiesToParentIcdAction.class;
    }

    @Override
    public Mono<MoveEntitiesToParentIcdResult> handleRequest(MoveEntitiesToParentIcdAction request,
                                                          ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}