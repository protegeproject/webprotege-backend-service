package edu.stanford.protege.webprotege.forms;

import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.IndexUpdaterServiceTestConfiguration;
import edu.stanford.protege.webprotege.MockingUtils;
import edu.stanford.protege.webprotege.MongoTestExtension;
import edu.stanford.protege.webprotege.WebprotegeBackendMonolithApplication;
import edu.stanford.protege.webprotege.common.LangTagFilter;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.project.ProjectComponentFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest(properties = "webprotege.rabbitmq.commands-subscribe=false")
@Import({WebprotegeBackendMonolithApplication.class, IndexUpdaterServiceTestConfiguration.class})
@ExtendWith({MongoTestExtension.class})
class GetEntityFormsActionHandlerIT {

    @Autowired
    ProjectComponentFactory projectComponentFactory;

    @Test
    void shouldCreateHandler() {
        var projectId = ProjectId.generate();
        var component = projectComponentFactory.createProjectComponent(projectId);
        var registry = component.getActionHandlerRegistry();
        var action = new GetEntityFormsAction(projectId,
                                              MockingUtils.mockOWLClass(),
                                              ImmutableSet.of(),
                                              LangTagFilter.get(ImmutableSet.of()),
                                              ImmutableSet.of(),
                                              ImmutableSet.of(),
                                              ImmutableSet.of());
        var actionHandler = registry.getActionHandler(action);
        actionHandler.execute(action,
                              new ExecutionContext());
    }
}