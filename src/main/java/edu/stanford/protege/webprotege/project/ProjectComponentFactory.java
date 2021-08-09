package edu.stanford.protege.webprotege.project;

import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.inject.ProjectComponent;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24 Jan 2018
 */
public interface ProjectComponentFactory {

    @Nonnull
    ProjectComponent createProjectComponent(@Nonnull ProjectId projectId);

}
