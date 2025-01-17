package edu.stanford.protege.webprotege.project;

import edu.stanford.protege.webprotege.IndexUpdaterServiceTestConfiguration;
import edu.stanford.protege.webprotege.MongoTestExtension;
import edu.stanford.protege.webprotege.WebprotegeBackendMonolithApplication;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeIpcApplication;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.extension.ExtendWith;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;

import java.io.IOException;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;

@SpringBootTest(properties = "webprotege.rabbitmq.commands-subscribe=false")
@Import({WebprotegeBackendMonolithApplication.class, IndexUpdaterServiceTestConfiguration.class, WebProtegeIpcApplication.class})
@ExtendWith({MongoTestExtension.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class ProjectCache_Test {

    @Autowired
    ProjectCache projectCache;

    @Test
    public void shouldInstantiateAndCacheAndPurgeProjectComponent() {
        var projectId = ProjectId.valueOf(UUID.randomUUID().toString());
        var revisionManager = projectCache.getRevisionManager(projectId);
        assertThat(revisionManager, is(notNullValue()));
        var revisionManagerSecond = projectCache.getRevisionManager(projectId);
        assertThat(revisionManagerSecond, is(revisionManager));
    }

    @Test
    public void shouldPurgeProject() {
        var projectId = ProjectId.valueOf(UUID.randomUUID().toString());
        projectCache.getRevisionManager(projectId);
        assertThat(projectCache.isActive(projectId), is(true));
        projectCache.purge(projectId);
        assertThat(projectCache.isActive(projectId), is(false));
    }

    @Test
    public void shouldPurgeAllProjects() {
        var projectId = ProjectId.valueOf(UUID.randomUUID().toString());
        projectCache.getRevisionManager(projectId);
        assertThat(projectCache.isActive(projectId), is(true));
        projectCache.purgeAllProjects();
        assertThat(projectCache.isActive(projectId), is(false));
    }

    @AfterEach
    public void tearDown() {
        projectCache.purgeAllProjects();
    }
}