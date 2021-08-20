package edu.stanford.protege.webprotege.project;

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
public class GetProjectDetailsCommandHandler implements CommandHandler<GetProjectDetailsAction, GetProjectDetailsResult> {

    private final ActionExecutor executor;

    public GetProjectDetailsCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return GetProjectDetailsAction.CHANNEL;
    }

    @Override
    public Class<GetProjectDetailsAction> getRequestClass() {
        return GetProjectDetailsAction.class;
    }

    @Override
    public Mono<GetProjectDetailsResult> handleRequest(GetProjectDetailsAction request,
                                                       ExecutionContext executionContext) {
        return Mono.just(executor.execute(request, executionContext));
    }
}