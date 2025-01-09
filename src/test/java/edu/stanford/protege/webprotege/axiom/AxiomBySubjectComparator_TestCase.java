package edu.stanford.protege.webprotege.axiom;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLObject;

import java.util.Comparator;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 03/02/15
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AxiomBySubjectComparator_TestCase {

    @Mock
    private Comparator<OWLObject> axiomSubjectComparator;

    @Mock
    private AxiomSubjectProvider axiomSubjectProvider;

    @Mock
    private OWLAxiom axiom1, axiom2;

    @Mock
    private OWLObject subject1, subject2;

    private AxiomBySubjectComparator comparator;

    @BeforeEach
    public void setUp() throws Exception {
        comparator = new AxiomBySubjectComparator(axiomSubjectProvider, axiomSubjectComparator);
        Mockito.doReturn(Optional.of(subject1)).when(axiomSubjectProvider).getSubject(axiom1);
        Mockito.doReturn(Optional.of(subject2)).when(axiomSubjectProvider).getSubject(axiom2);
    }

    @Test
    public void shouldReturnSubjectComparatorValue() {
        when(axiomSubjectComparator.compare(subject1, subject2)).thenReturn(5);
        assertThat(comparator.compare(axiom1, axiom2), is(5));
    }


    @Test
    public void shouldReturnZero() {
        Mockito.doReturn(Optional.empty()).when(axiomSubjectProvider).getSubject(axiom1);
        Mockito.doReturn(Optional.empty()).when(axiomSubjectProvider).getSubject(axiom2);
        assertThat(comparator.compare(axiom1, axiom2), is(0));
    }

    @Test
    public void shouldReturnMinusOne() {
        Mockito.doReturn(Optional.of(subject1)).when(axiomSubjectProvider).getSubject(axiom1);
        Mockito.doReturn(Optional.empty()).when(axiomSubjectProvider).getSubject(axiom2);
        assertThat(comparator.compare(axiom1, axiom2), is(-1));
    }

    @Test
    public void shouldReturnPlusOne() {
        Mockito.doReturn(Optional.empty()).when(axiomSubjectProvider).getSubject(axiom1);
        Mockito.doReturn(Optional.of(subject2)).when(axiomSubjectProvider).getSubject(axiom2);
        assertThat(comparator.compare(axiom1, axiom2), is(1));
    }
}
