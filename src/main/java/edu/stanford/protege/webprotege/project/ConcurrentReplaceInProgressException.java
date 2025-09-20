package edu.stanford.protege.webprotege.project;

import edu.stanford.protege.webprotege.common.ProjectId;

/**
 * Thrown when a revision history replace is requested while another replace is already
 * in progress for the same {@link ProjectId} within the current JVM.
 */
public class ConcurrentReplaceInProgressException extends IllegalStateException {

    private final ProjectId projectId;

    public ConcurrentReplaceInProgressException(ProjectId projectId) {
        super("Revision replace already in progress for project " + projectId.id());
        this.projectId = projectId;
    }

    public ProjectId getProjectId() {
        return projectId;
    }
}
