package edu.stanford.protege.webprotege.project;

import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.user.UserId;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ProjectCache_Test {

    @Autowired
    ProjectCache projectCache;

    @Test
    public void shouldInstantiateAndCacheAndPurgeProjectComponent() {
        var projectId = ProjectId.get(UUID.randomUUID().toString());
        var revisionManager = projectCache.getRevisionManager(projectId);
        assertThat(revisionManager, is(notNullValue()));
        var revisionManagerSecond = projectCache.getRevisionManager(projectId);
        assertThat(revisionManagerSecond, is(revisionManager));
    }

    @Test
    public void shouldPurgeProject() {
        var projectId = ProjectId.get(UUID.randomUUID().toString());
        projectCache.getRevisionManager(projectId);
        assertThat(projectCache.isActive(projectId), is(true));
        projectCache.purge(projectId);
        assertThat(projectCache.isActive(projectId), is(false));
    }

    @Test
    public void shouldPurgeAllProjects() {
        var projectId = ProjectId.get(UUID.randomUUID().toString());
        projectCache.getRevisionManager(projectId);
        assertThat(projectCache.isActive(projectId), is(true));
        projectCache.purgeAllProjects();
        assertThat(projectCache.isActive(projectId), is(false));
    }

    @Test
    public void shouldCreateNewEmptyProject() throws OWLOntologyCreationException, IOException {
        var projectId = projectCache.getProject(NewProjectSettings.get(UserId.getUserId("Matthew"),
                                                       "A project",
                                                       "en",
                                                       "A project description"
        ));
        assertThat(projectCache.isActive(projectId), is(true));
    }

    @After
    public void tearDown() {
        projectCache.purgeAllProjects();
    }
}