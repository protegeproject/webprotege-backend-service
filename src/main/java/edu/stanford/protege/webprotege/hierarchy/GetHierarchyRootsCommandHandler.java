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
public class GetHierarchyRootsCommandHandler implements CommandHandler<GetHierarchyRootsAction, GetHierarchyRootsResult> {

    private final ActionExecutor executor;

    public GetHierarchyRootsCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @Nonnull
    @Override
    public String getChannelName() {
        return GetHierarchyRootsAction.CHANNEL;
    }

    @Override
    public Class<GetHierarchyRootsAction> getRequestClass() {
        return GetHierarchyRootsAction.class;
    }

    @Override
    public Mono<GetHierarchyRootsResult> handleRequest(GetHierarchyRootsAction request,
                                                       ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}