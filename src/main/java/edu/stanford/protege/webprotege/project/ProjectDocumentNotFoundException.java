package edu.stanford.protege.webprotege.project;


import edu.stanford.protege.webprotege.common.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 13/05/2012
 * <p>
 *     Describes the situation where the project document cannot be found on the webprotege.
 * </p>
 */
public class ProjectDocumentNotFoundException extends RuntimeException {

    private final ProjectId projectId;

    public ProjectDocumentNotFoundException(ProjectId projectId) {
        super("Project Not Found (" + projectId + ")");
        this.projectId = projectId;
    }

    public ProjectId getProjectId() {
        return projectId;
    }
}
