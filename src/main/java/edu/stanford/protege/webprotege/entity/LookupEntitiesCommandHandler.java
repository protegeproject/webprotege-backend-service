package edu.stanford.protege.webprotege.entity;

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
public class LookupEntitiesCommandHandler implements CommandHandler<LookupEntitiesAction, LookupEntitiesResult> {

    private final ActionExecutor executor;

    public LookupEntitiesCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return LookupEntitiesAction.CHANNEL;
    }

    @Override
    public Class<LookupEntitiesAction> getRequestClass() {
        return LookupEntitiesAction.class;
    }

    @Override
    public Mono<LookupEntitiesResult> handleRequest(LookupEntitiesAction request, ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}