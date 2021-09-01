package edu.stanford.protege.webprotege.project;

import edu.stanford.protege.webprotege.common.ProjectId;

import javax.annotation.Nonnull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/01/2013
 */
public interface HasProjectId {

    /**
     * Get the {@link ProjectId}.
     * @return The {@link ProjectId}.  Not {@code null}.
     */
    @Nonnull
    ProjectId projectId();
}
