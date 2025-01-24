package edu.stanford.protege.webprotege.change;

import edu.stanford.protege.webprotege.owlapi.RenameMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-29
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ChangeApplicationResult_TestCase<S> {

    private ChangeApplicationResult<S> result;

    @Mock
    private S subject;

    @Mock
    private OntologyChange change;

    private List<OntologyChange> changeList;

    @Mock
    private RenameMap renameMap;

    @BeforeEach
    public void setUp() {
        changeList = Collections.singletonList(change);
        result = new ChangeApplicationResult<>(subject,
                                               changeList,
                                               renameMap);
    }

    @Test
    public void shouldGetSuppliedSubject() {
        assertThat(result.getSubject(), is(subject));
    }

    @Test
    public void shouldGetSuppliedChangeList() {
        assertThat(result.getChangeList(), is(changeList));
    }

    @Test
    public void shouldGetSuppliedRenameMap() {
        assertThat(result.getRenameMap(), is(renameMap));
    }
}
