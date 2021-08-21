package edu.stanford.protege.webprotege.search;

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
public class PerformEntitySearchCommandHandler implements CommandHandler<PerformEntitySearchAction, PerformEntitySearchResult> {

    private final ActionExecutor executor;

    public PerformEntitySearchCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return PerformEntitySearchAction.CHANNEL;
    }

    @Override
    public Class<PerformEntitySearchAction> getRequestClass() {
        return PerformEntitySearchAction.class;
    }

    @Override
    public Mono<PerformEntitySearchResult> handleRequest(PerformEntitySearchAction request,
                                                         ExecutionContext executionContext) {
        return Mono.just(executor.execute(request, executionContext));
    }
}