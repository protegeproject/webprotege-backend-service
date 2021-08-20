package edu.stanford.protege.webprotege.event;

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
public class GetProjectEventsCommandHandler implements CommandHandler<GetProjectEventsAction, GetProjectEventsResult> {

    private final ActionExecutor executor;

    public GetProjectEventsCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return GetProjectEventsAction.CHANNEL;
    }

    @Override
    public Class<GetProjectEventsAction> getRequestClass() {
        return GetProjectEventsAction.class;
    }

    @Override
    public Mono<GetProjectEventsResult> handleRequest(GetProjectEventsAction request,
                                                      ExecutionContext executionContext) {
        return Mono.just(executor.execute(request, executionContext));
    }
}