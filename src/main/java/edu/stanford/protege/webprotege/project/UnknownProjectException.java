package edu.stanford.protege.webprotege.project;


import edu.stanford.protege.webprotege.common.ProjectId;

import javax.annotation.Nonnull;
import java.io.Serializable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 02/04/2012
 */
public class UnknownProjectException extends RuntimeException implements Serializable, HasProjectId {

    private ProjectId projectId;

    /**
     * For serialization purposes only
     */
    private UnknownProjectException() {
    }

    /**
     * Creates an {@link UnknownProjectException} for the specified {@link ProjectId}
     * @param projectId The {@link ProjectId}.  Not {@code null}.
     * @throws NullPointerException if {@code projectId} is {@code null}.
     */
    public UnknownProjectException(ProjectId projectId) {
        this.projectId = checkNotNull(projectId);
    }

    /**
     * Get the {@link ProjectId} of the unknown project.
     * @return The {@link ProjectId}.  Not {@code null}.
     */
    @Nonnull
    public ProjectId projectId() {
        return projectId;
    }
}
