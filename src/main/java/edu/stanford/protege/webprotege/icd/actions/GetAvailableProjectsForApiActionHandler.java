package edu.stanford.protege.webprotege.icd.actions;

import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.dispatch.*;
import edu.stanford.protege.webprotege.dispatch.validators.NullValidator;
import edu.stanford.protege.webprotege.icd.dtos.ProjectSummaryDto;
import edu.stanford.protege.webprotege.icd.projects.GetReproducibleProjectsRequest;
import edu.stanford.protege.webprotege.icd.projects.GetReproducibleProjectsResponse;
import edu.stanford.protege.webprotege.icd.projects.ReproducibleProject;
import edu.stanford.protege.webprotege.ipc.*;
import edu.stanford.protege.webprotege.permissions.ProjectPermissionsManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static java.util.stream.Collectors.toList;


@WebProtegeHandler
public class GetAvailableProjectsForApiActionHandler implements ApplicationActionHandler<GetAvailableProjectsForApiAction, GetAvailableProjectsForApiResult> {

    private final ProjectPermissionsManager projectPermissionsManager;

    private final CommandExecutor<GetReproducibleProjectsRequest, GetReproducibleProjectsResponse> reproducibleProjectsExecutor;

    private final static Logger LOGGER = LoggerFactory.getLogger(GetAvailableProjectsForApiActionHandler.class);

    @Inject
    public GetAvailableProjectsForApiActionHandler(@Nonnull ProjectPermissionsManager projectPermissionsManager,
                                                   CommandExecutor<GetReproducibleProjectsRequest, GetReproducibleProjectsResponse> reproducibleProjectsExecutor) {
        this.projectPermissionsManager = projectPermissionsManager;
        this.reproducibleProjectsExecutor = reproducibleProjectsExecutor;
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


        try {
            List<ReproducibleProject> reproducibleProjectList = reproducibleProjectsExecutor.execute(new GetReproducibleProjectsRequest(), executionContext).get().reproducibleProjectList();
            List<ProjectSummaryDto> availableProjects = projectPermissionsManager.getReadableProjects(executionContext).stream()
                    .map(
                            details -> ProjectSummaryDto.create(
                                    details.projectId().id(),
                                    details.getDisplayName(),
                                    details.getCreatedAt(),
                                    details.getDescription(),
                                    getReproducibleProject(reproducibleProjectList, details.projectId()).map(ReproducibleProject::associatedBranch)
                                            .orElse("unknownBranch"))
                    ).collect(toList());
            return new GetAvailableProjectsForApiResult(availableProjects);

        } catch (InterruptedException | ExecutionException e) {
            LOGGER.error("Error fetching reproducible projects", e);
            throw new RuntimeException(e);
        }
    }

    private Optional<ReproducibleProject> getReproducibleProject(List<ReproducibleProject> availableProjects, ProjectId projectId){
        return availableProjects.stream().filter(p -> p.projectId().equals(projectId.id())).findFirst();
    }
}
