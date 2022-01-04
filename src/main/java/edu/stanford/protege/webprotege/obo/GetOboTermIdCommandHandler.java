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
public class GetOboTermIdCommandHandler implements CommandHandler<GetOboTermIdAction, GetOboTermIdResult> {

    private final ActionExecutor executor;

    public GetOboTermIdCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @Nonnull
    @Override
    public String getChannelName() {
        return GetOboTermIdAction.CHANNEL;
    }

    @Override
    public Class<GetOboTermIdAction> getRequestClass() {
        return GetOboTermIdAction.class;
    }

    @Override
    public Mono<GetOboTermIdResult> handleRequest(GetOboTermIdAction request, ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}