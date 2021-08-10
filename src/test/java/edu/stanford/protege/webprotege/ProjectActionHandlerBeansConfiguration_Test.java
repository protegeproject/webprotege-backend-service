package edu.stanford.protege.webprotege;

import edu.stanford.protege.webprotege.project.GetAvailableProjectsAction;
import edu.stanford.protege.webprotege.project.ProjectComponentFactory;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-07-12
 */
@SpringBootTest
public class ProjectActionHandlerBeansConfiguration_Test {

    @Autowired
    ProjectComponentFactory projectComponentFactory;

    @Test
    public void shouldProvideProjectActionHandlers() {
        var projectId = ProjectId.valueOf(UUID.randomUUID().toString());
        var projectComponent = projectComponentFactory.createProjectComponent(projectId);
        var actionHandlerRegistry = projectComponent.getActionHandlerRegistry();
        var handler = actionHandlerRegistry.getActionHandler(GetAvailableProjectsAction.create());
        assertThat(handler, is(Matchers.notNullValue()));
    }
}
