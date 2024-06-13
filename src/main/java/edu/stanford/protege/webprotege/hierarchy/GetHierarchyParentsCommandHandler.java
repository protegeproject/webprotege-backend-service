package edu.stanford.protege.webprotege.hierarchy;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-08-20
 */
@WebProtegeHandler
public class GetHierarchyParentsCommandHandler implements CommandHandler<GetHierarchyParentsAction, GetHierarchyParentsResult> {

    private final ActionExecutor executor;

    public GetHierarchyParentsCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @Nonnull
    @Override
    public String getChannelName() {
        return GetHierarchyParentsAction.CHANNEL;
    }

    @Override
    public Class<GetHierarchyParentsAction> getRequestClass() {
        return GetHierarchyParentsAction.class;
    }

    @Override
    public Mono<GetHierarchyParentsResult> handleRequest(GetHierarchyParentsAction request,
                                                          ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}