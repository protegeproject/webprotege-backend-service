package edu.stanford.protege.webprotege.icd.actions;


import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

@WebProtegeHandler
public class ValidateLogicalDefinitionsFromApiCommandHandler implements CommandHandler<ValidateLogicalDefinitionsFromApiAction, ValidateLogicalDefinitionsFromApiResult> {

    private final ActionExecutor executor;

    public ValidateLogicalDefinitionsFromApiCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }


    @NotNull
    @Override
    public String getChannelName() {
        return ValidateLogicalDefinitionsFromApiAction.CHANNEL;
    }

    @Override
    public Class<ValidateLogicalDefinitionsFromApiAction> getRequestClass() {
        return ValidateLogicalDefinitionsFromApiAction.class;
    }

    @Override
    public Mono<ValidateLogicalDefinitionsFromApiResult> handleRequest(ValidateLogicalDefinitionsFromApiAction request, ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}
