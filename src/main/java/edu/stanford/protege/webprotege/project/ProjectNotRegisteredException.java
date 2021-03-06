package edu.stanford.protege.webprotege.project;

import edu.stanford.protege.webprotege.common.ProjectId;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/06/2012
 * <p>
 *     Describes the situation where an operation could not be completed because the operation relied on a particular
 *     project being registered.
 * </p>
 */
public class ProjectNotRegisteredException extends RuntimeException implements Serializable {

    private ProjectId projectId;

    private ProjectNotRegisteredException() {
    }

    public ProjectNotRegisteredException(ProjectId projectId) {
        this.projectId = projectId;
    }

    public ProjectId getProjectId() {
        return projectId;
    }
}
