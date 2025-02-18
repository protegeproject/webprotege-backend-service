package edu.stanford.protege.webprotege.app;

import edu.stanford.protege.webprotege.*;
import edu.stanford.protege.webprotege.inject.ProjectComponent;
import edu.stanford.protege.webprotege.project.ProjectComponentFactory;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24 Jan 2018
 */
public class ProjectComponentFactoryImpl implements ProjectComponentFactory {

    protected static final String PROJECT_ID_BEAN_NAME = "projectId";

    private final ApplicationContext applicationContext;

    @Inject
    public ProjectComponentFactoryImpl(@Nonnull ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Nonnull
    @Override
    public ProjectComponent createProjectComponent(@Nonnull ProjectId projectId) {
        var projectContext = getProjectContext(projectId);
        return projectContext.getBean(ProjectComponent.class);
    }

    @Nonnull
    @Override
    public ApplicationContext getProjectContext(@Nonnull ProjectId projectId) {
        var projectContext = new AnnotationConfigApplicationContext();
        projectContext.setParent(applicationContext);
        projectContext.setId("ProjectContext-" + projectId.id());
        projectContext.setDisplayName("Project-Context for " + projectId);
        projectContext.register(ProjectBeansConfiguration.class);
        projectContext.register(ProjectIndexBeansConfiguration.class);
        projectContext.register(LuceneBeansConfiguration.class);
        projectContext.register(EntityMatcherBeansConfiguration.class);
        projectContext.register(ProjectActionHandlerBeansConfiguration.class);
        projectContext.register(FormBeansConfiguration.class);
        projectContext.registerBean(PROJECT_ID_BEAN_NAME, ProjectId.class, projectId.id());
        projectContext.refresh();
        return projectContext;
    }
}
