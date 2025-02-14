package edu.stanford.protege.webprotege.logicaldefinitions;


import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

@WebProtegeHandler
public class UpdateLogicalDefinitionsCommandHandler implements CommandHandler<UpdateLogicalDefinitionsAction, UpdateLogicalDefinitionsResponse > {

    private final ActionExecutor executor;

    public UpdateLogicalDefinitionsCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }


    @NotNull
    @Override
    public String getChannelName() {
        return UpdateLogicalDefinitionsAction.CHANNEL;
    }

    @Override
    public Class<UpdateLogicalDefinitionsAction> getRequestClass() {
        return UpdateLogicalDefinitionsAction.class;
    }

    @Override
    public Mono<UpdateLogicalDefinitionsResponse> handleRequest(UpdateLogicalDefinitionsAction request, ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}
