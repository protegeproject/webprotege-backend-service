package edu.stanford.protege.webprotege.icd.actions;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.*;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

@WebProtegeHandler
public class FilterExistingEntitiesCommandHandler implements CommandHandler<FilterExistingEntitiesAction, FilterExistingEntitiesResult> {

    private final ActionExecutor executor;

    public FilterExistingEntitiesCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return FilterExistingEntitiesAction.CHANNEL;
    }

    @Override
    public Class<FilterExistingEntitiesAction> getRequestClass() {
        return FilterExistingEntitiesAction.class;
    }

    @Override
    public Mono<FilterExistingEntitiesResult> handleRequest(FilterExistingEntitiesAction request, ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}
