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
 * 2021-08-20
 */
@WebProtegeHandler
public class GetHierarchyChildrenCommandHandler implements CommandHandler<GetHierarchyChildrenAction, GetHierarchyChildrenResult> {

    private final ActionExecutor executor;

    public GetHierarchyChildrenCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return GetHierarchyChildrenAction.CHANNEL;
    }

    @Override
    public Class<GetHierarchyChildrenAction> getRequestClass() {
        return GetHierarchyChildrenAction.class;
    }

    @Override
    public Mono<GetHierarchyChildrenResult> handleRequest(GetHierarchyChildrenAction request,
                                                          ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}