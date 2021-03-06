package edu.stanford.protege.webprotege.project;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import javax.annotation.Nonnull;
import reactor.core.publisher.Mono;


/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-08-19
 */
@WebProtegeHandler
public class GetAvailableProjectsCommandHandler implements CommandHandler<GetAvailableProjectsAction, GetAvailableProjectsResult> {


    private final ActionExecutor executor;

    public GetAvailableProjectsCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @Nonnull
    @Override
    public String getChannelName() {
        return GetAvailableProjectsAction.CHANNEL;
    }

    @Override
    public Class<GetAvailableProjectsAction> getRequestClass() {
        return GetAvailableProjectsAction.class;
    }

    @Override
    public Mono<GetAvailableProjectsResult> handleRequest(GetAvailableProjectsAction request,
                                                          ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}
