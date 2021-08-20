package edu.stanford.protege.webprotege.usage;

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
public class GetEntityUsageCommandHandler implements CommandHandler<GetEntityUsageAction, GetEntityUsageResult> {

    private final ActionExecutor executor;

    public GetEntityUsageCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return GetEntityUsageAction.CHANNEL;
    }

    @Override
    public Class<GetEntityUsageAction> getRequestClass() {
        return GetEntityUsageAction.class;
    }

    @Override
    public Mono<GetEntityUsageResult> handleRequest(GetEntityUsageAction request, ExecutionContext executionContext) {
        return Mono.just(executor.execute(request, executionContext));
    }
}