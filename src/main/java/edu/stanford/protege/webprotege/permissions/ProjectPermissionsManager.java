package edu.stanford.protege.webprotege.permissions;

import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.project.ProjectDetails;

import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
public interface ProjectPermissionsManager {

    /**
     * Gets a list of the projects that the specified user can read.  Note that these are the projects
     * for which the user has explicit read permission (i.e. projects that have essentially been shared
     * with the user).  The list does not include projects that are
     * world readable.
     * @param userId The userId.  Not {@code null}.
     * @return The (possibly empty) list of projects.
     */
    List<ProjectDetails> getReadableProjects(ExecutionContext executionContext);
}
