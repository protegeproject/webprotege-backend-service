package edu.stanford.protege.webprotege.icd.actions;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.*;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

@WebProtegeHandler
public class GetIsExistingProjectCommandHandler implements CommandHandler<GetIsExistingProjectAction, GetIsExistingProjectResult> {

    private final ActionExecutor executor;

    public GetIsExistingProjectCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return GetIsExistingProjectAction.CHANNEL;
    }

    @Override
    public Class<GetIsExistingProjectAction> getRequestClass() {
        return GetIsExistingProjectAction.class;
    }

    @Override
    public Mono<GetIsExistingProjectResult> handleRequest(GetIsExistingProjectAction request, ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}
