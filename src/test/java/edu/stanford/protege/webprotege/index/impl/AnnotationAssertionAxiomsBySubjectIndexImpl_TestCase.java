package edu.stanford.protege.webprotege.index.impl;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.change.AddAxiomChange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationSubject;
import org.semanticweb.owlapi.model.OWLOntologyID;

import static java.util.stream.Collectors.toSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-09
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AnnotationAssertionAxiomsBySubjectIndexImpl_TestCase {

    private AnnotationAssertionAxiomsBySubjectIndexImpl impl;

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private OWLAnnotationSubject subject;

    @Mock
    private OWLAnnotationAssertionAxiom axiom;

    @BeforeEach
    public void setUp() {
        when(axiom.getSubject())
                .thenReturn(subject);
        impl = new AnnotationAssertionAxiomsBySubjectIndexImpl();
        impl.applyChanges(ImmutableList.of(AddAxiomChange.of(ontologyId, axiom)));
    }

    @Test
    public void shouldGetAnnotationAssertionBySubject() {
        var axioms = impl.getAxiomsForSubject(subject, ontologyId).collect(toSet());
        assertThat(axioms, hasItem(axiom));
    }

    @Test
    public void shouldReturnEmptyStreamForUnknownOntologyId() {
        var axioms = impl.getAxiomsForSubject(subject, mock(OWLOntologyID.class)).collect(toSet());
        assertThat(axioms.isEmpty(), is(true));
    }

    @Test
    public void shouldReturnEmptyStreamForUnknownSubject() {
        var axioms = impl.getAxiomsForSubject(mock(OWLAnnotationSubject.class), ontologyId).collect(toSet());
        assertThat(axioms.isEmpty(), is(true));
    }

    @SuppressWarnings("ConstantConditions" )
    @Test
    public void shouldThrowNpeIfSubjectIsNull() {
        assertThrows(NullPointerException.class, () -> {
            impl.getAxiomsForSubject(null, ontologyId);
        });
    }

    @SuppressWarnings("ConstantConditions" )
    @Test
    public void shouldThrowNpeIfOntologyIdIsNull() {
        assertThrows(NullPointerException.class, () -> {
            impl.getAxiomsForSubject(subject, null);
        });
    }

    @Test
    public void shouldResetIndex() {
        impl.reset();
        var axioms = impl.getAxiomsForSubject(subject, ontologyId).collect(toSet());
        assertThat(axioms.isEmpty(), is(true));
    }
}
