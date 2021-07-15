package edu.stanford.protege.webprotege;

import edu.stanford.protege.webprotege.project.GetAvailableProjectsAction;
import edu.stanford.protege.webprotege.project.ProjectComponentFactory;
import edu.stanford.protege.webprotege.project.ProjectId;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-07-12
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ProjectActionHandlerBeansConfiguration_Test {

    @Autowired
    ProjectComponentFactory projectComponentFactory;

    @Test
    public void shouldProvideProjectActionHandlers() {
        var projectId = ProjectId.get(UUID.randomUUID().toString());
        var projectComponent = projectComponentFactory.createProjectComponent(projectId);
        var actionHandlerRegistry = projectComponent.getActionHandlerRegistry();
        var handler = actionHandlerRegistry.getActionHandler(GetAvailableProjectsAction.create());
        assertThat(handler, is(Matchers.notNullValue()));
    }
}
