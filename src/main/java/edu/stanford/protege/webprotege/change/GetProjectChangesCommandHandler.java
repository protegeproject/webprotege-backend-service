package edu.stanford.protege.webprotege.change;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-08-20
 */
@WebProtegeHandler
public class GetProjectChangesCommandHandler implements CommandHandler<GetProjectChangesAction, GetProjectChangesResult> {

    private final ActionExecutor executor;

    public GetProjectChangesCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return GetProjectChangesAction.CHANNEL;
    }

    @Override
    public Class<GetProjectChangesAction> getRequestClass() {
        return GetProjectChangesAction.class;
    }

    @Override
    public Mono<GetProjectChangesResult> handleRequest(GetProjectChangesAction request,
                                                       ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}