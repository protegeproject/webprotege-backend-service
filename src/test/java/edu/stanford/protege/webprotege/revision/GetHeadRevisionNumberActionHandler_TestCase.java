package edu.stanford.protege.webprotege.revision;

import edu.stanford.protege.webprotege.MongoTestExtension;
import edu.stanford.protege.webprotege.RabbitTestExtension;
import edu.stanford.protege.webprotege.WebprotegeBackendMonolithApplication;
import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/02/15
 */
@SpringBootTest
@Import({WebprotegeBackendMonolithApplication.class})
@ExtendWith({RabbitTestExtension.class, MongoTestExtension.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class GetHeadRevisionNumberActionHandler_TestCase {

    private GetHeadRevisionNumberActionHandler handler;

    private GetHeadRevisionNumberAction action = new GetHeadRevisionNumberAction(ProjectId.generate());

    private ExecutionContext executionContext = new ExecutionContext(new UserId("1"), "DUMMY_JWT");

    @Mock
    private RevisionNumber revisionNumber;

    @Mock
    private RevisionManager revisionManager;

    @Mock
    private AccessManager accessManager;

    @BeforeEach
    public void setUp() throws Exception {
        handler = new GetHeadRevisionNumberActionHandler(accessManager, revisionManager);
        when(revisionManager.getCurrentRevision()).thenReturn(revisionNumber);
    }

    @Test
    public void shouldReturnProjectRevision() {
        GetHeadRevisionNumberResult result = handler.execute(action, executionContext);
        assertThat(result.revisionNumber()).isEqualTo(revisionNumber);
    }
}
