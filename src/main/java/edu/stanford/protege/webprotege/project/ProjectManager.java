package edu.stanford.protege.webprotege.project;

import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.dispatch.impl.ProjectActionHandlerRegistry;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.revision.RevisionManager;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.IOException;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 07/03/2012
 */
public class ProjectManager {

    private final ProjectCache projectCache;

    private final ProjectAccessManager projectAccessManager;

    @Inject
    public ProjectManager(@Nonnull ProjectCache projectCache,
                          @Nonnull ProjectAccessManager projectAccessManager) {
        this.projectCache = projectCache;
        this.projectAccessManager = projectAccessManager;
    }


    @Nonnull
    public ProjectActionHandlerRegistry getActionHandlerRegistry(@Nonnull ProjectId projectId) {
        return projectCache.getActionHandlerRegistry(checkNotNull(projectId));
    }

    /**
     * Requests that the specified project is loaded for the specified user.
     * @param projectId The project.
     * @param requestingUser The user that is requesting that the project is loaded.
     * @throws ProjectDocumentNotFoundException If there is no such project.
     */
    public void ensureProjectIsLoaded(@Nonnull ProjectId projectId,
                                      @Nonnull UserId requestingUser) throws ProjectDocumentNotFoundException {
        long currentTime = System.currentTimeMillis();
        projectAccessManager.logProjectAccess(projectId, requestingUser, currentTime);
        projectCache.ensureProjectIsLoaded(projectId);
    }

    public RevisionManager getRevisionManager(@Nonnull ProjectId projectId) {
        return projectCache.getRevisionManager(projectId);
    }

    public ProjectId createNewProject(@Nonnull ProjectId projectId,
                                      @Nonnull NewProjectSettings newProjectSettings,
                                      @Nonnull ExecutionContext executionContext) throws ProjectAlreadyExistsException, OWLOntologyCreationException, IOException, OWLOntologyStorageException {
        return projectCache.getProject(projectId, newProjectSettings, executionContext);
    }
}
