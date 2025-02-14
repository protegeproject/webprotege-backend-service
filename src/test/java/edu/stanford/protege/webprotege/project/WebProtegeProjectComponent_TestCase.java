package edu.stanford.protege.webprotege.project;

import edu.stanford.protege.webprotege.IndexUpdaterServiceTestConfiguration;
import edu.stanford.protege.webprotege.MongoTestExtension;
import edu.stanford.protege.webprotege.RabbitTestExtension;
import edu.stanford.protege.webprotege.WebprotegeBackendMonolithApplication;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.inject.ProjectComponent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;

import java.util.UUID;
import java.util.function.Function;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-07-12
 */
@SpringBootTest(properties = "webprotege.rabbitmq.commands-subscribe=false")
@Import({WebprotegeBackendMonolithApplication.class, IndexUpdaterServiceTestConfiguration.class})
@ExtendWith({MongoTestExtension.class, RabbitTestExtension.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class WebProtegeProjectComponent_TestCase {

    @Autowired
    ProjectComponentFactory projectComponentFactory;

    @Test
    public void shouldInstantiateProjectComponentFactory() {
        var projectIdIn = ProjectId.valueOf(UUID.randomUUID().toString());
        var projectComponent = projectComponentFactory.createProjectComponent(projectIdIn);
        var projectIdOut = projectComponent.getProjectId();
        assertThat(projectIdOut, is(projectIdIn));
        projectComponent.getDisposablesManager().dispose();
    }

    @Test
    public void shouldInstantiateAndDestroyProjectComponentFactory() {
        var projectIdIn = ProjectId.valueOf(UUID.randomUUID().toString());
        var projectComponent = projectComponentFactory.createProjectComponent(projectIdIn);
        var projectIdOut = projectComponent.getProjectId();
        assertThat(projectIdOut, is(projectIdIn));
        // Dispose of this project then instantiate it again.  This should be possible
        // even with the Lucene file based backend
        projectComponent.getDisposablesManager().dispose();
        var freshProjectComponent = projectComponentFactory.createProjectComponent(projectIdIn);
        freshProjectComponent.getDisposablesManager().dispose();
    }

    @Test
    public void shouldInstantiateFreshProjectComponentFactory() {
        var projectIdA = ProjectId.valueOf(UUID.randomUUID().toString());
        var projectIdB = ProjectId.valueOf(UUID.randomUUID().toString());
        var projectComponentA = projectComponentFactory.createProjectComponent(projectIdA);
        var projectComponentB = projectComponentFactory.createProjectComponent(projectIdB);
        assertThat(projectComponentA, is(not(projectComponentB)));
        assertThat(projectComponentA.getProjectId(), is(projectIdA));
        assertThat(projectComponentB.getProjectId(), is(projectIdB));
        projectComponentA.getDisposablesManager().dispose();
        projectComponentB.getDisposablesManager().dispose();
    }

    @Test
    public void shouldGetRevisionManagerAsSingleton() {
        shouldInstantiateSingleton(ProjectComponent::getRevisionManager);
    }

    @Test
    public void shouldGetActionHandlerRegistry() {
        shouldInstantiateSingleton(ProjectComponent::getActionHandlerRegistry);
    }

    private void shouldInstantiateSingleton(Function<ProjectComponent, Object> objectProvider) {
        var projectId = ProjectId.valueOf(UUID.randomUUID().toString());
        var projectComponent = projectComponentFactory.createProjectComponent(projectId);
        var object = objectProvider.apply(projectComponent);
        assertThat(object, is(not(nullValue())));
        var object2 = objectProvider.apply(projectComponent);
        assertThat(object, is(object2));

        // TODO: Instantiating the same project twice causes Lucene to throw an exception

//        var otherProjectComponent = projectComponentFactory.createProjectComponent(projectId);
//        var otherProjectComponentObject = objectProvider.apply(otherProjectComponent);
//        assertThat(otherProjectComponentObject, is(not(object)));
//        projectComponent.getDisposablesManager().dispose();
//        otherProjectComponent.getDisposablesManager().dispose();
    }

    @AfterEach
    public void tearDown() throws Exception {

    }
}
