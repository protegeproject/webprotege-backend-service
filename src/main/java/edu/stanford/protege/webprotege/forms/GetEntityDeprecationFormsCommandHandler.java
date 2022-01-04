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
public class GetEntityDeprecationFormsCommandHandler implements CommandHandler<GetEntityDeprecationFormsAction, GetEntityDeprecationFormsResult> {

    private final ActionExecutor executor;

    public GetEntityDeprecationFormsCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @Nonnull
    @Override
    public String getChannelName() {
        return GetEntityDeprecationFormsAction.CHANNEL;
    }

    @Override
    public Class<GetEntityDeprecationFormsAction> getRequestClass() {
        return GetEntityDeprecationFormsAction.class;
    }

    @Override
    public Mono<GetEntityDeprecationFormsResult> handleRequest(GetEntityDeprecationFormsAction request,
                                                               ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}