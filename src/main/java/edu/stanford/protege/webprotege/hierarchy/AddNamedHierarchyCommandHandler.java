package edu.stanford.protege.webprotege.hierarchy;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

@WebProtegeHandler
public class AddNamedHierarchyCommandHandler implements CommandHandler<AddNamedHierarchyAction, AddNamedHierarchyResponse> {

    private final ActionExecutor actionExecutor;

    public AddNamedHierarchyCommandHandler(ActionExecutor actionExecutor) {
        this.actionExecutor = actionExecutor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return AddNamedHierarchyAction.CHANNEL;
    }

    @Override
    public Class<AddNamedHierarchyAction> getRequestClass() {
        return AddNamedHierarchyAction.class;
    }

    @Override
    public Mono<AddNamedHierarchyResponse> handleRequest(AddNamedHierarchyAction request, ExecutionContext executionContext) {
        return actionExecutor.executeRequest(request, executionContext);
    }
}

