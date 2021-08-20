package edu.stanford.protege.webprotege.search;

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
public class GetSearchSettingsCommandHandler implements CommandHandler<GetSearchSettingsAction, GetSearchSettingsResult> {

    private final ActionExecutor executor;

    public GetSearchSettingsCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return GetSearchSettingsAction.CHANNEL;
    }

    @Override
    public Class<GetSearchSettingsAction> getRequestClass() {
        return GetSearchSettingsAction.class;
    }

    @Override
    public Mono<GetSearchSettingsResult> handleRequest(GetSearchSettingsAction request,
                                                       ExecutionContext executionContext) {
        return Mono.just(executor.execute(request, executionContext));
    }
}