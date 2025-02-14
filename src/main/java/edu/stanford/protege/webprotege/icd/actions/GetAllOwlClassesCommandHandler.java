package edu.stanford.protege.webprotege.icd.actions;


import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;

@WebProtegeHandler
public class GetAllOwlClassesCommandHandler implements CommandHandler<GetAllOwlClassesAction, GetAllOwlClassesResult> {

    private final ActionExecutor executor;

    public GetAllOwlClassesCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @Nonnull
    @Override
    public String getChannelName() {
        return GetAllOwlClassesAction.CHANNEL;
    }

    @Override
    public Class<GetAllOwlClassesAction> getRequestClass() {
        return GetAllOwlClassesAction.class;
    }

    @Override
    public Mono<GetAllOwlClassesResult> handleRequest(GetAllOwlClassesAction request,
                                                                ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}
