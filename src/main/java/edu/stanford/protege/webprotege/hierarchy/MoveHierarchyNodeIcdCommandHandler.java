package edu.stanford.protege.webprotege.hierarchy;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;

@WebProtegeHandler
public class MoveHierarchyNodeIcdCommandHandler implements CommandHandler<MoveHierarchyNodeIcdAction, MoveHierarchyNodeIcdResult> {

    private final ActionExecutor executor;

    public MoveHierarchyNodeIcdCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @Nonnull
    @Override
    public String getChannelName() {
        return MoveHierarchyNodeIcdAction.CHANNEL;
    }

    @Override
    public Class<MoveHierarchyNodeIcdAction> getRequestClass() {
        return MoveHierarchyNodeIcdAction.class;
    }

    @Override
    public Mono<MoveHierarchyNodeIcdResult> handleRequest(MoveHierarchyNodeIcdAction request,
                                                       ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}