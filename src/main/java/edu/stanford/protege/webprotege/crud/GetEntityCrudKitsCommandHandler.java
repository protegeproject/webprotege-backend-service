package edu.stanford.protege.webprotege.crud;

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
public class GetEntityCrudKitsCommandHandler implements CommandHandler<GetEntityCrudKitsAction, GetEntityCrudKitsResult> {

    private final ActionExecutor executor;

    public GetEntityCrudKitsCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return GetEntityCrudKitsAction.CHANNEL;
    }

    @Override
    public Class<GetEntityCrudKitsAction> getRequestClass() {
        return GetEntityCrudKitsAction.class;
    }

    @Override
    public Mono<GetEntityCrudKitsResult> handleRequest(GetEntityCrudKitsAction request,
                                                       ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}