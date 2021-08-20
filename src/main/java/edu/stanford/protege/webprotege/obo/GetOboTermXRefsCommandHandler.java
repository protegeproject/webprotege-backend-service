package edu.stanford.protege.webprotege.obo;

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
public class GetOboTermXRefsCommandHandler implements CommandHandler<GetOboTermXRefsAction, GetOboTermXRefsResult> {

    private final ActionExecutor executor;

    public GetOboTermXRefsCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return GetOboTermXRefsAction.CHANNEL;
    }

    @Override
    public Class<GetOboTermXRefsAction> getRequestClass() {
        return GetOboTermXRefsAction.class;
    }

    @Override
    public Mono<GetOboTermXRefsResult> handleRequest(GetOboTermXRefsAction request, ExecutionContext executionContext) {
        return Mono.just(executor.execute(request, executionContext));
    }
}