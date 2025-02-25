package edu.stanford.protege.webprotege.permissions;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.authorization.Resource;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.project.ProjectDetails;
import edu.stanford.protege.webprotege.project.ProjectDetailsRepository;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static edu.stanford.protege.webprotege.access.BuiltInAction.VIEW_PROJECT;
import static edu.stanford.protege.webprotege.authorization.Subject.forUser;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 06/02/15
 */
public class ProjectPermissionsManagerImpl implements ProjectPermissionsManager {

    private final AccessManager accessManager;

    private final ProjectDetailsRepository projectDetailsRepository;


    @Inject
    public ProjectPermissionsManagerImpl(@Nonnull AccessManager accessManager,
                                         @Nonnull ProjectDetailsRepository projectDetailsRepository) {
        this.accessManager = accessManager;
        this.projectDetailsRepository = projectDetailsRepository;
    }

    @Override
    public List<ProjectDetails> getReadableProjects(ExecutionContext executionContext) {
        Set<ProjectDetails> result = new HashSet<>();
        accessManager.getResourcesAccessibleToSubject(forUser(executionContext.userId()), VIEW_PROJECT.getActionId(), new ExecutionContext(executionContext.userId(), executionContext.jwt()))
                     .stream()
                     .filter(Resource::isProject)
                     .forEach(
                             resource ->
                                     resource.getProjectId()
                                             .flatMap(projectDetailsRepository::findOne)
                                             .ifPresent(result::add));
        // Always add owned in case permissions are screwed up - yes?
        // It will be obvious that the permissions are screwed up because the
        // user won't be able to open their own project.
        result.addAll(projectDetailsRepository.findByOwner(executionContext.userId()));
        return new ArrayList<>(result);
    }
}
