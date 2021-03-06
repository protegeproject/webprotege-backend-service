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
public class GetEntityCreationFormsCommandHandler implements CommandHandler<GetEntityCreationFormsAction, GetEntityCreationFormsResult> {

    private final ActionExecutor executor;

    public GetEntityCreationFormsCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @Nonnull
    @Override
    public String getChannelName() {
        return GetEntityCreationFormsAction.CHANNEL;
    }

    @Override
    public Class<GetEntityCreationFormsAction> getRequestClass() {
        return GetEntityCreationFormsAction.class;
    }

    @Override
    public Mono<GetEntityCreationFormsResult> handleRequest(GetEntityCreationFormsAction request,
                                                            ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}