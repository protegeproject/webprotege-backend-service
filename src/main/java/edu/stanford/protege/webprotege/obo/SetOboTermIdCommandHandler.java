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
 * 2021-08-21
 */
@WebProtegeHandler
public class SetOboTermIdCommandHandler implements CommandHandler<SetOboTermIdAction, SetOboTermIdResult> {

    private final ActionExecutor executor;

    public SetOboTermIdCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @Nonnull
    @Override
    public String getChannelName() {
        return SetOboTermIdAction.CHANNEL;
    }

    @Override
    public Class<SetOboTermIdAction> getRequestClass() {
        return SetOboTermIdAction.class;
    }

    @Override
    public Mono<SetOboTermIdResult> handleRequest(SetOboTermIdAction request, ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}