package edu.stanford.protege.webprotege.hierarchy;

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
public class MoveHierarchyNodeCommandHandler implements CommandHandler<MoveHierarchyNodeAction, MoveHierarchyNodeResult> {

    private final ActionExecutor executor;

    public MoveHierarchyNodeCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return MoveHierarchyNodeAction.CHANNEL;
    }

    @Override
    public Class<MoveHierarchyNodeAction> getRequestClass() {
        return MoveHierarchyNodeAction.class;
    }

    @Override
    public Mono<MoveHierarchyNodeResult> handleRequest(MoveHierarchyNodeAction request,
                                                       ExecutionContext executionContext) {
        return Mono.just(executor.execute(request, executionContext));
    }
}