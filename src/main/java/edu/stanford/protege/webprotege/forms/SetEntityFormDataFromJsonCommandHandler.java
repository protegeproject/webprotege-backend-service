package edu.stanford.protege.webprotege.forms;


import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

@WebProtegeHandler
public class SetEntityFormDataFromJsonCommandHandler implements CommandHandler<SetEntityFormDataFromJsonAction, SetEntityFormDataFromJsonResult> {


    private final ActionExecutor executor;

    public SetEntityFormDataFromJsonCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return SetEntityFormDataFromJsonAction.CHANNEL;
    }

    @Override
    public Class<SetEntityFormDataFromJsonAction> getRequestClass() {
        return SetEntityFormDataFromJsonAction.class;
    }

    @Override
    public Mono<SetEntityFormDataFromJsonResult> handleRequest(SetEntityFormDataFromJsonAction request, ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}
