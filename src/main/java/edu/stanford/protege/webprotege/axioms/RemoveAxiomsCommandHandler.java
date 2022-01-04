package edu.stanford.protege.webprotege.axioms;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import javax.annotation.Nonnull;
import reactor.core.publisher.Mono;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-09-01
 */
@WebProtegeHandler
public class RemoveAxiomsCommandHandler implements CommandHandler<RemoveAxiomsRequest, RemoveAxiomsResponse> {

    private final ActionExecutor actionExecutor;

    public RemoveAxiomsCommandHandler(ActionExecutor actionExecutor) {
        this.actionExecutor = actionExecutor;
    }

    @Nonnull
    @Override
    public String getChannelName() {
        return RemoveAxiomsRequest.CHANNEL;
    }

    @Override
    public Class<RemoveAxiomsRequest> getRequestClass() {
        return RemoveAxiomsRequest.class;
    }

    @Override
    public Mono<RemoveAxiomsResponse> handleRequest(RemoveAxiomsRequest request, ExecutionContext executionContext) {
        return actionExecutor.executeRequest(request, executionContext);
    }
}
