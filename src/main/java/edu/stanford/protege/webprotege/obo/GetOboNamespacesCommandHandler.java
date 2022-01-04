package edu.stanford.protege.webprotege.obo;

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
public class GetOboNamespacesCommandHandler implements CommandHandler<GetOboNamespacesAction, GetOboNamespacesResult> {

    private final ActionExecutor executor;

    public GetOboNamespacesCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @Nonnull
    @Override
    public String getChannelName() {
        return GetOboNamespacesAction.CHANNEL;
    }

    @Override
    public Class<GetOboNamespacesAction> getRequestClass() {
        return GetOboNamespacesAction.class;
    }

    @Override
    public Mono<GetOboNamespacesResult> handleRequest(GetOboNamespacesAction request,
                                                      ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}