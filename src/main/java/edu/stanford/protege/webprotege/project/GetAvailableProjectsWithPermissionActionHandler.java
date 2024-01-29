package edu.stanford.protege.webprotege.project;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.authorization.Resource;
import edu.stanford.protege.webprotege.authorization.Subject;
import edu.stanford.protege.webprotege.dispatch.ApplicationActionHandler;
import edu.stanford.protege.webprotege.dispatch.ExecutionContext;
import edu.stanford.protege.webprotege.dispatch.RequestContext;
import edu.stanford.protege.webprotege.dispatch.RequestValidator;
import edu.stanford.protege.webprotege.dispatch.validators.NullValidator;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-14
 */
public class GetAvailableProjectsWithPermissionActionHandler implements ApplicationActionHandler<GetAvailableProjectsWithPermissionAction, GetAvailableProjectsWithPermissionResult> {

    @Nonnull
    private final AccessManager accessManager;

    @Nonnull
    private final ProjectDetailsManager projectDetailsManager;

    @Inject
    public GetAvailableProjectsWithPermissionActionHandler(@Nonnull AccessManager accessManager,
                                                           @Nonnull ProjectDetailsManager projectDetailsManager) {
        this.accessManager = accessManager;
        this.projectDetailsManager = projectDetailsManager;
    }

    @Nonnull
    @Override
    public Class<GetAvailableProjectsWithPermissionAction> getActionClass() {
        return GetAvailableProjectsWithPermissionAction.class;
    }

    @Nonnull
    @Override
    public RequestValidator getRequestValidator(@Nonnull GetAvailableProjectsWithPermissionAction action,
                                                @Nonnull RequestContext requestContext) {
        return NullValidator.get();
    }

    @Nonnull
    @Override
    public GetAvailableProjectsWithPermissionResult execute(@Nonnull GetAvailableProjectsWithPermissionAction action,
                                                            @Nonnull ExecutionContext executionContext) {
        var userId = Subject.forUser(executionContext.getUserId());
        var permission = action.getPermission();
        var projectDetails = accessManager.getResourcesAccessibleToSubject(userId, permission, new edu.stanford.protege.webprotege.ipc.ExecutionContext(executionContext.getUserId(), executionContext.getJwt()))
                     .stream()
                     .map(Resource::getProjectId)
                     .filter(Optional::isPresent)
                     .map(Optional::get)
                     .map(projectDetailsManager::getProjectDetails)
                     .collect(toImmutableList());
        return GetAvailableProjectsWithPermissionResult.create(projectDetails);
    }
}
