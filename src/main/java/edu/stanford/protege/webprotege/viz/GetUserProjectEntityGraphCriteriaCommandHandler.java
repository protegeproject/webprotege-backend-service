package edu.stanford.protege.webprotege.viz;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-08-21
 */
@WebProtegeHandler
public class GetUserProjectEntityGraphCriteriaCommandHandler implements CommandHandler<GetUserProjectEntityGraphCriteriaAction, GetUserProjectEntityGraphCriteriaResult> {

    private final ActionExecutor executor;

    public GetUserProjectEntityGraphCriteriaCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return GetUserProjectEntityGraphCriteriaAction.CHANNEL;
    }

    @Override
    public Class<GetUserProjectEntityGraphCriteriaAction> getRequestClass() {
        return GetUserProjectEntityGraphCriteriaAction.class;
    }

    @Override
    public Mono<GetUserProjectEntityGraphCriteriaResult> handleRequest(GetUserProjectEntityGraphCriteriaAction request,
                                                                       ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}