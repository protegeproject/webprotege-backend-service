package edu.stanford.protege.webprotege.icd.actions;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.*;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;


@WebProtegeHandler
public class GetClassAncestorsWithLinearizationCommandHandler implements CommandHandler<GetClassAncestorsWithLinearizationAction, GetClassAncestorsWithLinearizationResult> {
    private final ActionExecutor executor;

    public GetClassAncestorsWithLinearizationCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return GetClassAncestorsWithLinearizationAction.CHANNEL;
    }

    @Override
    public Class<GetClassAncestorsWithLinearizationAction> getRequestClass() {
        return GetClassAncestorsWithLinearizationAction.class;
    }

    @Override
    public Mono<GetClassAncestorsWithLinearizationResult> handleRequest(GetClassAncestorsWithLinearizationAction request, ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}
