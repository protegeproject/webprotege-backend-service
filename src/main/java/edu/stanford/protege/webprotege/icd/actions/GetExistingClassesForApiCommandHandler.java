package edu.stanford.protege.webprotege.icd.actions;


import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

@WebProtegeHandler

public class GetExistingClassesForApiCommandHandler implements CommandHandler<GetExistingClassesForApiAction, GetExistingClassesForApiResult> {
    private final ActionExecutor executor;

    public GetExistingClassesForApiCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return GetExistingClassesForApiAction.CHANNEL;
    }

    @Override
    public Class<GetExistingClassesForApiAction> getRequestClass() {
        return GetExistingClassesForApiAction.class;
    }

    @Override
    public Mono<GetExistingClassesForApiResult> handleRequest(GetExistingClassesForApiAction request, ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}
