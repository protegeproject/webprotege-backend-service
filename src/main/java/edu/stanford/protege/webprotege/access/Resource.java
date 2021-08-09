package edu.stanford.protege.webprotege.access;

import edu.stanford.protege.webprotege.common.ProjectId;

import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Jan 2017
 */
public interface Resource {

    Optional<ProjectId> getProjectId();

    boolean isApplication();

    boolean isProject();

    boolean isProject(ProjectId projectId);
}

