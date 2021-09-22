package edu.stanford.protege.webprotege.inject.project;

import edu.stanford.protege.webprotege.common.ProjectId;

import javax.inject.Inject;
import javax.inject.Provider;
import java.io.File;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/03/15
 */
public class ProjectDirectoryProvider implements Provider<File> {

    private final ProjectDirectoryFactory projectDirectoryFactory;

    private final ProjectId projectId;

    @Inject
    public ProjectDirectoryProvider(ProjectDirectoryFactory projectDirectoryFactory, ProjectId projectId) {
        this.projectDirectoryFactory = projectDirectoryFactory;
        this.projectId = projectId;
    }

    @Override
    public File get() {
        return projectDirectoryFactory.getProjectDirectory(projectId);
    }
}
