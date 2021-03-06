package edu.stanford.protege.webprotege.forms;

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
public class GetEntityFormsCommandHandler implements CommandHandler<GetEntityFormsAction, GetEntityFormsResult> {

    private final ActionExecutor executor;

    public GetEntityFormsCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @Nonnull
    @Override
    public String getChannelName() {
        return GetEntityFormsAction.CHANNEL;
    }

    @Override
    public Class<GetEntityFormsAction> getRequestClass() {
        return GetEntityFormsAction.class;
    }

    @Override
    public Mono<GetEntityFormsResult> handleRequest(GetEntityFormsAction request, ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}