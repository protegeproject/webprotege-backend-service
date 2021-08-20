package edu.stanford.protege.webprotege.permissions;

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
public class GetProjectPermissionsCommandHandler implements CommandHandler<GetProjectPermissionsAction, GetProjectPermissionsResult> {

    private final ActionExecutor executor;

    public GetProjectPermissionsCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return GetProjectPermissionsAction.CHANNEL;
    }

    @Override
    public Class<GetProjectPermissionsAction> getRequestClass() {
        return GetProjectPermissionsAction.class;
    }

    @Override
    public Mono<GetProjectPermissionsResult> handleRequest(GetProjectPermissionsAction request,
                                                           ExecutionContext executionContext) {
        return Mono.just(executor.execute(request, executionContext));
    }
}