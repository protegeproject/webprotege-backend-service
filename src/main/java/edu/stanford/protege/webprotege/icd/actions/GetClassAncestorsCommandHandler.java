package edu.stanford.protege.webprotege.icd.actions;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;


@WebProtegeHandler
public class GetClassAncestorsCommandHandler  implements CommandHandler<GetClassAncestorsAction, GetClassAncestorsResult> {
    private final ActionExecutor executor;

    public GetClassAncestorsCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return GetClassAncestorsAction.CHANNEL;
    }

    @Override
    public Class<GetClassAncestorsAction> getRequestClass() {
        return GetClassAncestorsAction.class;
    }

    @Override
    public Mono<GetClassAncestorsResult> handleRequest(GetClassAncestorsAction request, ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}
