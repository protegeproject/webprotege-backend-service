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
 * 2021-09-01
 */
@WebProtegeHandler
public class GetDeprecatedEntitiesCommandHandler implements CommandHandler<GetDeprecatedEntitiesAction, GetDeprecatedEntitiesResult> {

    private final ActionExecutor executor;

    public GetDeprecatedEntitiesCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return GetDeprecatedEntitiesAction.CHANNEL;
    }

    @Override
    public Class<GetDeprecatedEntitiesAction> getRequestClass() {
        return GetDeprecatedEntitiesAction.class;
    }

    @Override
    public Mono<GetDeprecatedEntitiesResult> handleRequest(GetDeprecatedEntitiesAction request,
                                                           ExecutionContext executionContext) {
        return Mono.just(executor.execute(request, executionContext));
    }
}