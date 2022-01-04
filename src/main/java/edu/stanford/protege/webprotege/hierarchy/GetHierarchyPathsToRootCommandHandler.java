package edu.stanford.protege.webprotege.hierarchy;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import javax.annotation.Nonnull;
import reactor.core.publisher.Mono;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-08-20
 */
@WebProtegeHandler
public class GetHierarchyPathsToRootCommandHandler implements CommandHandler<GetHierarchyPathsToRootAction, GetHierarchyPathsToRootResult> {

    private final ActionExecutor executor;

    public GetHierarchyPathsToRootCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @Nonnull
    @Override
    public String getChannelName() {
        return GetHierarchyPathsToRootAction.CHANNEL;
    }

    @Override
    public Class<GetHierarchyPathsToRootAction> getRequestClass() {
        return GetHierarchyPathsToRootAction.class;
    }

    @Override
    public Mono<GetHierarchyPathsToRootResult> handleRequest(GetHierarchyPathsToRootAction request,
                                                             ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}