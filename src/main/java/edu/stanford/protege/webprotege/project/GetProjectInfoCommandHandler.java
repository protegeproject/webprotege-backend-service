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
public class GetProjectInfoCommandHandler implements CommandHandler<GetProjectInfoAction, GetProjectInfoResult> {

    private final ActionExecutor executor;

    public GetProjectInfoCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return GetProjectInfoAction.CHANNEL;
    }

    @Override
    public Class<GetProjectInfoAction> getRequestClass() {
        return GetProjectInfoAction.class;
    }

    @Override
    public Mono<GetProjectInfoResult> handleRequest(GetProjectInfoAction request, ExecutionContext executionContext) {
        return Mono.just(executor.execute(request, executionContext));
    }
}