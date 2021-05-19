package edu.stanford.protege.webprotege.shortform;

import dagger.Module;
import dagger.Provides;
import edu.stanford.protege.webprotege.inject.ProjectSingleton;
import edu.stanford.protege.webprotege.project.ProjectId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-14
 */
@Module
public class ProjectIdModule {

    private final ProjectId projectId;

    public ProjectIdModule(ProjectId projectId) {
        this.projectId = projectId;
    }


    @Provides
    @ProjectSingleton
    public ProjectId getProjectId() {
        return projectId;
    }
}
