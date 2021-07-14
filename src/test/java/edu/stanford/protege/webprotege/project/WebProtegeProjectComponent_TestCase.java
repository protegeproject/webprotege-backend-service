package edu.stanford.protege.webprotege.project;

import edu.stanford.protege.webprotege.inject.DataDirectory;
import edu.stanford.protege.webprotege.inject.ProjectComponent;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;
import java.util.function.Function;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-07-12
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class WebProtegeProjectComponent_TestCase {

    @Autowired
    ProjectComponentFactory projectComponentFactory;

    @Test
    public void shouldInstantiateProjectComponentFactory() {
        var projectIdIn = ProjectId.get(UUID.randomUUID().toString());
        var projectComponent = projectComponentFactory.createProjectComponent(projectIdIn);
        var projectIdOut = projectComponent.getProjectId();
        assertThat(projectIdOut, is(projectIdIn));
        projectComponent.getDisposablesManager().dispose();
    }

    @Test
    public void shouldInstantiateAndDestroyProjectComponentFactory() {
        var projectIdIn = ProjectId.get(UUID.randomUUID().toString());
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
        var projectIdA = ProjectId.get(UUID.randomUUID().toString());
        var projectIdB = ProjectId.get(UUID.randomUUID().toString());
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
    public void shouldGetEventManagerAsSingleton() {
        shouldInstantiateSingleton(ProjectComponent::getEventManager);
    }

    @Test
    public void shouldGetActionHandlerRegistry() {
        shouldInstantiateSingleton(ProjectComponent::getActionHandlerRegistry);
    }

    private void shouldInstantiateSingleton(Function<ProjectComponent, Object> objectProvider) {
        var projectId = ProjectId.get(UUID.randomUUID().toString());
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

    @After
    public void tearDown() throws Exception {

    }
}
