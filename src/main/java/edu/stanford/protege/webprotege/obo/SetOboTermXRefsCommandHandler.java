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
public class SetOboTermXRefsCommandHandler implements CommandHandler<SetOboTermXRefsAction, SetOboTermXRefsResult> {

    private final ActionExecutor executor;

    public SetOboTermXRefsCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @Nonnull
    @Override
    public String getChannelName() {
        return SetOboTermXRefsAction.CHANNEL;
    }

    @Override
    public Class<SetOboTermXRefsAction> getRequestClass() {
        return SetOboTermXRefsAction.class;
    }

    @Override
    public Mono<SetOboTermXRefsResult> handleRequest(SetOboTermXRefsAction request, ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}