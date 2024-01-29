package edu.stanford.protege.webprotege.project;

import edu.stanford.protege.webprotege.dispatch.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import javax.annotation.Nonnull;
import reactor.core.publisher.Mono;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-08-19
 */
public class GetAvailableProjectsWithPermissionCommandHandler implements CommandHandler<GetAvailableProjectsWithPermissionAction, GetAvailableProjectsWithPermissionResult> {

    private final GetAvailableProjectsWithPermissionActionHandler delegate;

    public GetAvailableProjectsWithPermissionCommandHandler(GetAvailableProjectsWithPermissionActionHandler delegate) {
        this.delegate = delegate;
    }

    @Nonnull
    @Override
    public String getChannelName() {
        return "projects.GetAvailableProjectsWithPermission";
    }

    @Override
    public Class<GetAvailableProjectsWithPermissionAction> getRequestClass() {
        return GetAvailableProjectsWithPermissionAction.class;
    }

    @Override
    public Mono<GetAvailableProjectsWithPermissionResult> handleRequest(GetAvailableProjectsWithPermissionAction request,
                                                                        edu.stanford.protege.webprotege.ipc.ExecutionContext executionContext) {
        var result = delegate.execute(request, new ExecutionContext(executionContext.userId(), executionContext.jwt()));
        return Mono.just(result);
    }
}
