package edu.stanford.protege.webprotege.index.impl;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.change.*;
import edu.stanford.protege.webprotege.index.OntologyAnnotationsIndex;
import edu.stanford.protege.webprotege.index.OntologyAxiomsIndex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntologyID;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-09-18
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class RootIndexImpl_TestCase {

    private RootIndexImpl impl;

    @Mock
    private OntologyAxiomsIndex ontologyAxiomsIndex;

    @Mock
    private OntologyAnnotationsIndex ontologyAnnotationIndex;

    @Mock
    private OWLAxiom axiom;

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private OWLAnnotation annotation;

    @BeforeEach
    public void setUp() {
        when(ontologyAxiomsIndex.containsAxiom(axiom, ontologyId))
                .thenReturn(true);
        when(ontologyAnnotationIndex.containsAnnotation(annotation, ontologyId))
                .thenReturn(true);
        impl = new RootIndexImpl(ontologyAxiomsIndex,
                                 ontologyAnnotationIndex);
    }

    @Test
    public void shouldNotAddAxiomInOntology() {
        var change = AddAxiomChange.of(ontologyId, axiom);
        var effectiveChanges = getEffectiveChanges(change);
        assertThat(effectiveChanges, is(empty()));
    }

    @Test
    public void shouldAddAxiomNotInOntology() {
        var otherAxiom = mock(OWLAxiom.class);
        var change = AddAxiomChange.of(ontologyId, otherAxiom);
        var effectiveChanges = getEffectiveChanges(change);
        assertThat(effectiveChanges, contains(change));
    }

    @Test
    public void shouldRemoveAxiomInOntology() {
        var change = RemoveAxiomChange.of(ontologyId, axiom);
        var effectiveChanges = getEffectiveChanges(change);
        assertThat(effectiveChanges, contains(change));
    }

    @Test
    public void shouldNotRemoveAxiomNotInOntology() {
        var otherAxiom = mock(OWLAxiom.class);
        var change = RemoveAxiomChange.of(ontologyId, otherAxiom);
        var effectiveChanges = getEffectiveChanges(change);
        assertThat(effectiveChanges, is(empty()));
    }


    @Test
    public void shouldNotAddAnnotationInOntology() {
        var change = AddOntologyAnnotationChange.of(ontologyId, annotation);
        var effectiveChanges = getEffectiveChanges(change);
        assertThat(effectiveChanges, is(empty()));
    }

    @Test
    public void shouldAddAnnotationNotInOntology() {
        var otherAnnotation = mock(OWLAnnotation.class);
        var change = AddOntologyAnnotationChange.of(ontologyId, otherAnnotation);
        var effectiveChanges = getEffectiveChanges(change);
        assertThat(effectiveChanges, contains(change));
    }

    @Test
    public void shouldRemoveAnnotationInOntology() {
        var change = RemoveOntologyAnnotationChange.of(ontologyId, annotation);
        var effectiveChanges = getEffectiveChanges(change);
        assertThat(effectiveChanges, contains(change));
    }

    @Test
    public void shouldNotRemoveAnnotationNotInOntology() {
        var otherAnnotation = mock(OWLAnnotation.class);
        var change = RemoveOntologyAnnotationChange.of(ontologyId, otherAnnotation);
        var effectiveChanges = getEffectiveChanges(change);
        assertThat(effectiveChanges, is(empty()));
    }

    @Test
    public void shouldFilterOutDuplicateChanges() {
        var otherAxiom = mock(OWLAxiom.class);
        var firstChange = AddAxiomChange.of(ontologyId, otherAxiom);
        var secondChange = AddAxiomChange.of(ontologyId, otherAxiom);
        var changes = ImmutableList.<OntologyChange>of(
                firstChange,
                secondChange
        );
        var effectiveChanges = impl.getEffectiveChanges(changes);
        assertThat(effectiveChanges, contains(firstChange));
        assertThat(effectiveChanges.size(), is(1));
    }

    @Test
    public void shouldFilterOutCancellingChanges() {
        var otherAxiom = mock(OWLAxiom.class);
        var firstChange = AddAxiomChange.of(ontologyId, otherAxiom);
        var secondChange = RemoveAxiomChange.of(ontologyId, otherAxiom);
        var changes = ImmutableList.<OntologyChange>of(
                firstChange,
                secondChange
        );
        var effectiveChanges = impl.getEffectiveChanges(changes);
        assertThat(effectiveChanges, is(empty()));
    }

    private List<OntologyChange> getEffectiveChanges(OntologyChange change) {
        var changes = getChangeList(change);
        return impl.getEffectiveChanges(changes);
    }

    private static ImmutableList<OntologyChange> getChangeList(OntologyChange change) {
        return ImmutableList.of(change);
    }
}
