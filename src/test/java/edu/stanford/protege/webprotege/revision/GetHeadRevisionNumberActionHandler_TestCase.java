package edu.stanford.protege.webprotege.revision;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/02/15
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class GetHeadRevisionNumberActionHandler_TestCase {

    private GetHeadRevisionNumberActionHandler handler;

    private GetHeadRevisionNumberAction action = new GetHeadRevisionNumberAction(ProjectId.generate());

    private ExecutionContext executionContext = new ExecutionContext(new UserId("1"), "DUMMY_JWT", "correlationId");

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
