package edu.stanford.protege.webprotege.revision;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.dispatch.ExecutionContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/02/15
 */
@SpringBootTest
public class GetHeadRevisionNumberActionHandler_TestCase {

    private GetHeadRevisionNumberActionHandler handler;

    @Mock
    private GetHeadRevisionNumberAction action;

    @Mock
    private ExecutionContext executionContext;

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
