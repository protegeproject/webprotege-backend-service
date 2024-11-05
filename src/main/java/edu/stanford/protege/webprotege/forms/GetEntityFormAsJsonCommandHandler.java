package edu.stanford.protege.webprotege.forms;


import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

@WebProtegeHandler
public class GetEntityFormAsJsonCommandHandler  implements CommandHandler<GetEntityFormAsJsonAction, GetEntityFormAsJsonResult> {

    private final ActionExecutor executor;

    public GetEntityFormAsJsonCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }


    @NotNull
    @Override
    public String getChannelName() {
        return GetEntityFormAsJsonAction.CHANNEL;
    }

    @Override
    public Class<GetEntityFormAsJsonAction> getRequestClass() {
        return GetEntityFormAsJsonAction.class;
    }

    @Override
    public Mono<GetEntityFormAsJsonResult> handleRequest(GetEntityFormAsJsonAction request, ExecutionContext executionContext) {
        return this.executor.executeRequest(request, executionContext);
    }
}
