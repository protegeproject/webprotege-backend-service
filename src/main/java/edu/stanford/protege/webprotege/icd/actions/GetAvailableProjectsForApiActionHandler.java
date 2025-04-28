package edu.stanford.protege.webprotege.icd.actions;

import edu.stanford.protege.webprotege.dispatch.*;
import edu.stanford.protege.webprotege.dispatch.validators.NullValidator;
import edu.stanford.protege.webprotege.icd.dtos.ProjectSummaryDto;
import edu.stanford.protege.webprotege.ipc.*;
import edu.stanford.protege.webprotege.permissions.ProjectPermissionsManager;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;

import static java.util.stream.Collectors.toList;


@WebProtegeHandler
public class GetAvailableProjectsForApiActionHandler implements ApplicationActionHandler<GetAvailableProjectsForApiAction, GetAvailableProjectsForApiResult> {

    private final ProjectPermissionsManager projectPermissionsManager;

    @Inject
    public GetAvailableProjectsForApiActionHandler(@Nonnull ProjectPermissionsManager projectPermissionsManager) {
        this.projectPermissionsManager = projectPermissionsManager;
    }

    @Nonnull
    @Override
    public Class<GetAvailableProjectsForApiAction> getActionClass() {
        return GetAvailableProjectsForApiAction.class;
    }

    @Nonnull
    @Override
    public RequestValidator getRequestValidator(@Nonnull GetAvailableProjectsForApiAction action, @Nonnull RequestContext requestContext) {
        return NullValidator.get();
    }

    @Nonnull
    @Override
    public GetAvailableProjectsForApiResult execute(@Nonnull GetAvailableProjectsForApiAction action, @Nonnull ExecutionContext executionContext) {
        List<ProjectSummaryDto> availableProjects = projectPermissionsManager.getReadableProjects(executionContext).stream()
                .map(
                        details -> ProjectSummaryDto.create(
                                details.projectId().id(),
                                details.getDisplayName(),
                                details.getCreatedAt(),
                                details.getDescription())
                ).collect(toList());
        return new GetAvailableProjectsForApiResult(availableProjects);
    }
}
