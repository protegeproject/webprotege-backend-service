package edu.stanford.protege.webprotege.icd.actions;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.*;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;


@WebProtegeHandler
public class GetAvailableProjectsForApiCommandHandler implements CommandHandler<GetAvailableProjectsForApiAction, GetAvailableProjectsForApiResult> {


    private final ActionExecutor executor;

    public GetAvailableProjectsForApiCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @Nonnull
    @Override
    public String getChannelName() {
        return GetAvailableProjectsForApiAction.CHANNEL;
    }

    @Override
    public Class<GetAvailableProjectsForApiAction> getRequestClass() {
        return GetAvailableProjectsForApiAction.class;
    }

    @Override
    public Mono<GetAvailableProjectsForApiResult> handleRequest(GetAvailableProjectsForApiAction request,
                                                                ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}
