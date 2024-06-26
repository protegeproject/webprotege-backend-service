package edu.stanford.protege.webprotege.project;

import edu.stanford.protege.webprotege.common.BlobLocation;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.ipc.CommandExecutor;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ontology.ProcessUploadedOntologiesRequest;
import edu.stanford.protege.webprotege.ontology.ProcessUploadedOntologiesResponse;
import edu.stanford.protege.webprotege.revision.CreateInitialRevisionHistoryRequest;
import edu.stanford.protege.webprotege.revision.CreateInitialRevisionHistoryResponse;
import edu.stanford.protege.webprotege.storage.MinioFileDownloader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2024-05-07
 */
@Component
public class CreateProjectSagaManager {

    private static final Logger logger = LoggerFactory.getLogger(CreateProjectSagaManager.class);

    private final ProjectDetailsManager projectDetailsManager;

    private final CommandExecutor<ProcessUploadedOntologiesRequest, ProcessUploadedOntologiesResponse> processOntologiesExecutor;

    private final CommandExecutor<CreateInitialRevisionHistoryRequest, CreateInitialRevisionHistoryResponse> createInitialRevisionHistoryExecutor;

    private final MinioFileDownloader fileDownloader;

    private final RevisionHistoryReplacer revisionHistoryReplacer;

    private final ProjectPermissionsInitializer projectPermissionsInitializer;


    public CreateProjectSagaManager(ProjectDetailsManager projectDetailsManager,
                                    CommandExecutor<ProcessUploadedOntologiesRequest, ProcessUploadedOntologiesResponse> processOntologiesExecutor,
                                    CommandExecutor<CreateInitialRevisionHistoryRequest, CreateInitialRevisionHistoryResponse> createInitialRevisionHistoryExecutor,
                                    MinioFileDownloader fileDownloader,
                                    RevisionHistoryReplacer revisionHistoryReplacer,
                                    ProjectPermissionsInitializer projectPermissionsInitializer) {
        this.projectDetailsManager = projectDetailsManager;
        this.processOntologiesExecutor = processOntologiesExecutor;
        this.createInitialRevisionHistoryExecutor = createInitialRevisionHistoryExecutor;
        this.revisionHistoryReplacer = revisionHistoryReplacer;
        this.fileDownloader = fileDownloader;
        this.projectPermissionsInitializer = projectPermissionsInitializer;
    }


    public CompletableFuture<CreateNewProjectResult> execute(NewProjectSettings newProjectSettings, ExecutionContext executionContext) {
        if (newProjectSettings.hasSourceDocument()) {
            return createProjectFromSources(new SagaStateWithSources(ProjectId.generate(), newProjectSettings, executionContext));
        }
        else {
            return createEmptyProject(new SagaState(ProjectId.generate(), newProjectSettings, executionContext));
        }
    }

    private CompletableFuture<CreateNewProjectResult> createEmptyProject(SagaState sagaState) {
        logger.info("Creating an empty project: {}", sagaState.newProjectSettings);
        return registerProject(sagaState).thenCompose(this::initializeProjectPermissions)
                                         .thenCompose(this::retrieveProjectDetails)
                                         .handle((r, e) -> {
                                             if (e == null) {
                                                 return new CreateNewProjectResult(r.getProjectDetails());
                                             }
                                             else {
                                                 // Should be a CompletionException
                                                 throw new ProjectCreationException(sagaState.getProjectId(),
                                                                                    "Project creation failed",
                                                                                    e.getCause());
                                             }
                                         });
    }

    private CompletableFuture<CreateNewProjectResult> createProjectFromSources(SagaStateWithSources sagaState) {
        logger.info("Creating a project from sources: {}", sagaState.getNewProjectSettings());
        return processSources(sagaState).thenCompose(this::createInitialRevisionHistory)
                                        .thenCompose(this::downloadRevisionHistory)
                                        .thenCompose(this::copyRevisionHistoryToProject)
                                        .thenCompose(this::registerProject)
                                        .thenCompose(this::initializeProjectPermissions)
                                        .thenCompose(this::retrieveProjectDetails)
                                        .handle((r, e) -> {
                                            if (e == null) {
                                                return new CreateNewProjectResult(r.getProjectDetails());
                                            }
                                            else {
                                                throw new ProjectCreationException(sagaState.getProjectId(),
                                                                                   "Project creation failed",
                                                                                   e.getCause());
                                            }
                                        });
    }

    private CompletableFuture<SagaState> retrieveProjectDetails(SagaState sagaState) {
        return CompletableFuture.supplyAsync(() -> projectDetailsManager.getProjectDetails(sagaState.getProjectId()))
                                .thenApply(sagaState::setProjectDetails);
    }

    private CompletableFuture<SagaState> initializeProjectPermissions(SagaState sagaState) {
        var projectOwner = sagaState.getNewProjectSettings().getProjectOwner();
        return projectPermissionsInitializer.applyDefaultPermissions(sagaState.getProjectId(), projectOwner)
                                            .thenApply(r -> sagaState);
    }

    private CompletableFuture<SagaStateWithSources> processSources(SagaStateWithSources sagaState) {
        var request = sagaState.createProcessUploadedOntologiesRequest();
        return processOntologiesExecutor.execute(request, sagaState.getExecutionContext())
                                        .thenApply(sagaState::handleProcessUploadedOntologiesResponse);
    }

    private CompletableFuture<SagaStateWithSources> createInitialRevisionHistory(SagaStateWithSources sagaState) {
        var createHistoryRequest = sagaState.createCreateInitialRevisionHistoryRequest();
        return createInitialRevisionHistoryExecutor.execute(createHistoryRequest, sagaState.getExecutionContext())
                                                   .thenApply(sagaState::handleCreateInitialRevisionHistoryResponse);
    }

    private CompletableFuture<SagaStateWithSources> downloadRevisionHistory(SagaStateWithSources sagaState) {
        return fileDownloader.downloadFile(sagaState.getRevisionHistoryLocation()).thenApply(sagaState::setRevisionHistoryPath);
    }

    private CompletableFuture<SagaStateWithSources> copyRevisionHistoryToProject(SagaStateWithSources sagaState) {
        return revisionHistoryReplacer.replaceRevisionHistory(sagaState.getProjectId(),
                                                              sagaState.getRevisionHistoryPath())
                                      .thenApply(r -> sagaState);
    }

    private CompletableFuture<SagaState> registerProject(SagaState sagaState) {
        return CompletableFuture.supplyAsync(() -> {
            projectDetailsManager.registerProject(sagaState.getProjectId(), sagaState.getNewProjectSettings());
            return sagaState;
        });
    }


    private static class SagaState {

        private final ProjectId projectId;

        private final NewProjectSettings newProjectSettings;

        private final ExecutionContext executionContext;

        private ProjectDetails projectDetails;

        private SagaState(ProjectId projectId, NewProjectSettings newProjectSettings, ExecutionContext executionContext) {
            this.projectId = Objects.requireNonNull(projectId);
            this.newProjectSettings = newProjectSettings;
            this.executionContext = executionContext;
        }

        public ProjectId getProjectId() {
            return projectId;
        }

        public NewProjectSettings getNewProjectSettings() {
            return newProjectSettings;
        }

        public ExecutionContext getExecutionContext() {
            return executionContext;
        }

        public SagaState setProjectDetails(ProjectDetails projectDetails) {
            this.projectDetails = projectDetails;
            return this;
        }

        public ProjectDetails getProjectDetails() {
            if (projectDetails == null) {
                throw  new IllegalStateException("ProjectDetails is null");
            }
            return projectDetails;
        }
    }

    private static class SagaStateWithSources extends SagaState {

        private List<BlobLocation> ontologyDocumentLocations;

        private BlobLocation revisionHistoryLocation;

        private Path revisionHistoryPath;

        public SagaStateWithSources(ProjectId projectId, NewProjectSettings newProjectSettings, ExecutionContext executionContext) {
            super(projectId, newProjectSettings, executionContext);
        }

        public BlobLocation getRevisionHistoryLocation() {
            return Objects.requireNonNull(revisionHistoryLocation);
        }

        public Path getRevisionHistoryPath() {
            return Objects.requireNonNull(revisionHistoryPath);
        }

        public SagaStateWithSources setRevisionHistoryPath(Path revisionHistoryPath) {
            this.revisionHistoryPath = revisionHistoryPath;
            return this;
        }

        public CreateInitialRevisionHistoryRequest createCreateInitialRevisionHistoryRequest() {
            return new CreateInitialRevisionHistoryRequest(ontologyDocumentLocations);
        }

        public SagaStateWithSources handleCreateInitialRevisionHistoryResponse(CreateInitialRevisionHistoryResponse response) {
            this.revisionHistoryLocation = response.revisionHistoryLocation();
            return this;
        }

        public ProcessUploadedOntologiesRequest createProcessUploadedOntologiesRequest() {
            return new ProcessUploadedOntologiesRequest(getNewProjectSettings().sourceDocument());
        }

        public SagaStateWithSources handleProcessUploadedOntologiesResponse(ProcessUploadedOntologiesResponse response) {
            this.ontologyDocumentLocations = response.ontologies();
            return this;
        }

    }

}
