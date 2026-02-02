package edu.stanford.protege.webprotege.icd.actions;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

@WebProtegeHandler
public class ValidateAxisBelongsToHierarchyCommandHandler implements CommandHandler<ValidateAxisBelongsToHierarchyAction, ValidateAxisBelongsToHierarchyResult> {

    private final ActionExecutor executor;

    public ValidateAxisBelongsToHierarchyCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return ValidateAxisBelongsToHierarchyAction.CHANNEL;
    }

    @Override
    public Class<ValidateAxisBelongsToHierarchyAction> getRequestClass() {
        return ValidateAxisBelongsToHierarchyAction.class;
    }

    @Override
    public Mono<ValidateAxisBelongsToHierarchyResult> handleRequest(ValidateAxisBelongsToHierarchyAction request, ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}
