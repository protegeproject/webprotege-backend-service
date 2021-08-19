package edu.stanford.protege.webprotege.project;

import edu.stanford.protege.webprotege.dispatch.ExecutionContext;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-08-19
 */
public class GetAvailableProjectsWithPermissionHandler implements edu.stanford.protege.webprotege.ipc.CommandHandler<GetAvailableProjectsWithPermissionAction, GetAvailableProjectsWithPermissionResult> {

    private GetAvailableProjectsWithPermissionActionHandler delegate;

    public GetAvailableProjectsWithPermissionHandler(GetAvailableProjectsWithPermissionActionHandler delegate) {
        this.delegate = delegate;
    }

    @NotNull
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
        var result = delegate.execute(request, new ExecutionContext(executionContext.userId()));
        return Mono.just(result);
    }
}
