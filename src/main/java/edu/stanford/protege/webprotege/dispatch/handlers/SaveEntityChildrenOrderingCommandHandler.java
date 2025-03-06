package edu.stanford.protege.webprotege.dispatch.handlers;


import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.dispatch.actions.SaveEntityChildrenOrderingAction;
import edu.stanford.protege.webprotege.dispatch.actions.SaveEntityChildrenOrderingResult;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

@WebProtegeHandler
public class SaveEntityChildrenOrderingCommandHandler  implements CommandHandler<SaveEntityChildrenOrderingAction, SaveEntityChildrenOrderingResult> {


    private final ActionExecutor actionExecutor;

    public SaveEntityChildrenOrderingCommandHandler(ActionExecutor actionExecutor) {
        this.actionExecutor = actionExecutor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return SaveEntityChildrenOrderingAction.CHANNEL;
    }

    @Override
    public Class<SaveEntityChildrenOrderingAction> getRequestClass() {
        return SaveEntityChildrenOrderingAction.class;
    }

    @Override
    public Mono<SaveEntityChildrenOrderingResult> handleRequest(SaveEntityChildrenOrderingAction request, ExecutionContext executionContext) {
        return actionExecutor.executeRequest(request, executionContext);
    }
}
