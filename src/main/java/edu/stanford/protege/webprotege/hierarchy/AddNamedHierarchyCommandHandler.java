package edu.stanford.protege.webprotege.hierarchy;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

@WebProtegeHandler
public class AddNamedHierarchyCommandHandler implements CommandHandler<SetNamedHierarchiesAction, SetNamedHierarchiesResponse> {

    private final ActionExecutor actionExecutor;

    public AddNamedHierarchyCommandHandler(ActionExecutor actionExecutor) {
        this.actionExecutor = actionExecutor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return SetNamedHierarchiesAction.CHANNEL;
    }

    @Override
    public Class<SetNamedHierarchiesAction> getRequestClass() {
        return SetNamedHierarchiesAction.class;
    }

    @Override
    public Mono<SetNamedHierarchiesResponse> handleRequest(SetNamedHierarchiesAction request, ExecutionContext executionContext) {
        return actionExecutor.executeRequest(request, executionContext);
    }
}

