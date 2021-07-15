package edu.stanford.protege.webprotege.app;

import edu.stanford.protege.webprotege.*;
import edu.stanford.protege.webprotege.inject.ProjectComponent;
import edu.stanford.protege.webprotege.project.ProjectComponentFactory;
import edu.stanford.protege.webprotege.project.ProjectId;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24 Jan 2018
 */
public class ProjectComponentFactoryImpl implements ProjectComponentFactory {

    private final ApplicationContext applicationContext;

    @Inject
    public ProjectComponentFactoryImpl(@Nonnull ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Nonnull
    @Override
    public ProjectComponent createProjectComponent(@Nonnull ProjectId projectId) {
        var projectContext = new AnnotationConfigApplicationContext();
        projectContext.setParent(applicationContext);
        projectContext.setDisplayName("Project-Context for " + projectId);
        projectContext.register(ProjectBeansConfiguration.class);
        projectContext.register(ProjectIndexesConfiguration.class);
        projectContext.register(LuceneConfiguration.class);
        projectContext.register(EntityMatcherConfiguration.class);
        projectContext.register(ProjectActionHandlersConfiguration.class);
        projectContext.register(OboBeansConfiguration.class);
        projectContext.register(FormBeansConfiguration.class);
        projectContext.registerBean("projectId", ProjectId.class, projectId.getId());
        projectContext.refresh();
        return projectContext.getBean(ProjectComponent.class);
    }
}
