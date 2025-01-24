package edu.stanford.protege.webprotege.change;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.common.ChangeRequestId;
import edu.stanford.protege.webprotege.owlapi.RenameMap;
import edu.stanford.protege.webprotege.revision.Revision;
import edu.stanford.protege.webprotege.revision.RevisionManager;
import edu.stanford.protege.webprotege.revision.RevisionNumber;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-29
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class RevisionReverterChangeListGenerator_TestCase {

    private RevisionReverterChangeListGenerator generator;

    private RevisionNumber revisionNumber = RevisionNumber.getRevisionNumber(3);

    @Mock
    private RevisionManager revisionManager;

    @Mock
    private Revision revision;

    @Mock
    private ChangeGenerationContext context;

    @Mock
    private OntologyChange changeO, inverseChange0, change1, inverseChange1;

    @BeforeEach
    public void setUp() {
        var changes = ImmutableList.of(changeO, change1);
        generator = new RevisionReverterChangeListGenerator(revisionNumber,
                                                            revisionManager,
                                                            ChangeRequestId.generate());
        when(revisionManager.getRevision(revisionNumber))
                .thenReturn(Optional.of(revision));

        when(revision.getChanges())
                .thenReturn(changes);

        when(changeO.getInverseChange())
                .thenReturn(inverseChange0);
        when(change1.getInverseChange())
                .thenReturn(inverseChange1);
    }

    @Test
    public void shouldGenerateInverseChangesInReverseOrder() {
        var ontologyChangeList = generator.generateChanges(context);
        var inverseChanges = ontologyChangeList.getChanges();
        assertThat(inverseChanges, contains(inverseChange1, inverseChange0));
    }

    @Test
    public void shouldGenerateEmptyListForUnknownRevision() {
        when(revisionManager.getRevision(revisionNumber))
                .thenReturn(Optional.empty());
        var ontologyChangeList = generator.generateChanges(context);
        var inverseChanges = ontologyChangeList.getChanges();
        assertThat(inverseChanges, is(empty()));

    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldGetMessageContainingRevisionNumber() {
        var message = generator.getMessage(mock(ChangeApplicationResult.class));
        assertThat(message, Matchers.containsString(" 3"));
    }

    @Test
    public void shouldGetRenamedResult() {
        var renameMap = mock(RenameMap.class);
        var result = generator.getRenamedResult(true, renameMap);
        assertThat(result, is(true));
    }
}
