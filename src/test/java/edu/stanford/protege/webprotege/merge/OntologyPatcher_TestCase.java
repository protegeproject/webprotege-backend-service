package edu.stanford.protege.webprotege.merge;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.change.*;
import edu.stanford.protege.webprotege.common.ChangeRequestId;
import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.diff.OntologyDiff2OntologyChanges;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-21
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class OntologyPatcher_TestCase {

    private OntologyPatcher patcher;

    @Mock
    private HasApplyChanges changeManager;

    @Mock
    private OntologyDiff2OntologyChanges ontologyDiff2OntologyChanges;

    @Mock
    private OntologyDiff ontologyDiff;

    private String commitMessage = "The Commit Message";

    private ExecutionContext executionContext;


    @Mock
    private OntologyChange ontologyChange;

    private UserId userId = edu.stanford.protege.webprotege.MockingUtils.mockUserId();

    @Captor
    private ArgumentCaptor<UserId> userIdCaptor;


    @Captor
    private ArgumentCaptor<ChangeListGenerator<?>> changeListGeneratorCaptor;

    @BeforeEach
    public void setUp() {
        patcher = new OntologyPatcher(changeManager,
                                      ontologyDiff2OntologyChanges);
        when(ontologyDiff2OntologyChanges.getOntologyChangesFromDiff(ontologyDiff))
                .thenReturn(ImmutableList.of(ontologyChange));
        executionContext = new ExecutionContext(userId, "DUMMY_JWT", "correlationId");
    }

    @Test
    public void shouldApplyChangesWithExecContextUser() {
        patcher.applyPatch(ChangeRequestId.generate(),
                           Collections.singleton(ontologyDiff),
                           commitMessage,
                           executionContext);

        verify(changeManager, times(1)).applyChanges(userIdCaptor.capture(), changeListGeneratorCaptor.capture());
        assertThat(userIdCaptor.getValue(), is(userId));
    }

    @Test
    public void shouldCreateChangeGeneratorForOntologyChanges() {
        patcher.applyPatch(ChangeRequestId.generate(),
                           Collections.singleton(ontologyDiff),
                           commitMessage,
                           executionContext);

        verify(changeManager, times(1)).applyChanges(userIdCaptor.capture(), changeListGeneratorCaptor.capture());

        var changeListGenerator = changeListGeneratorCaptor.getValue();
        var changes = changeListGenerator.generateChanges(new ChangeGenerationContext(userId));
        assertThat(changes.getChanges(), hasItem(ontologyChange));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldCreateChangeGeneratorWithSuppliedMessage() {
        patcher.applyPatch(ChangeRequestId.generate(),
                           Collections.singleton(ontologyDiff),
                           commitMessage,
                           executionContext);

        verify(changeManager, times(1)).applyChanges(userIdCaptor.capture(), changeListGeneratorCaptor.capture());

        var changeListGenerator = changeListGeneratorCaptor.getValue();
        var message = changeListGenerator.getMessage(mock(ChangeApplicationResult.class));
        assertThat(message, is(this.commitMessage));


    }
}
