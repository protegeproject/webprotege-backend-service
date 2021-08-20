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
public class GetHierarchySiblingsCommandHandler implements CommandHandler<GetHierarchySiblingsAction, GetHierarchySiblingsResult> {

    private final ActionExecutor executor;

    public GetHierarchySiblingsCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return GetHierarchySiblingsAction.CHANNEL;
    }

    @Override
    public Class<GetHierarchySiblingsAction> getRequestClass() {
        return GetHierarchySiblingsAction.class;
    }

    @Override
    public Mono<GetHierarchySiblingsResult> handleRequest(GetHierarchySiblingsAction request,
                                                          ExecutionContext executionContext) {
        return Mono.just(executor.execute(request, executionContext));
    }
}