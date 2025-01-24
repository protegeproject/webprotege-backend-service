package edu.stanford.protege.webprotege.merge;

import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.change.*;
import edu.stanford.protege.webprotege.diff.OntologyDiff2OntologyChanges;
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
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-21
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class OntologyDiff2Changes_TestCase {

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private OntologyDiff ontologyDiff;

    @Mock
    private Diff<OWLAxiom> axiomDiff;

    @Mock
    private OWLAxiom addedAxiom;

    @Mock
    private OWLAxiom removedAxiom;

    @Mock
    private Diff<OWLAnnotation> annotationDiff;

    @Mock
    private OWLAnnotation addedAnnotation;

    @Mock
    private OWLAnnotation removedAnnotation;


    private List<OntologyChange> ontologyChanges;

    private OntologyDiff2OntologyChanges diff2Changes;

    @BeforeEach
    public void setUp() {
        when(ontologyDiff.getAxiomDiff())
                .thenReturn(axiomDiff);

        when(ontologyDiff.getFromOntologyId())
                .thenReturn(ontologyId);



        when(axiomDiff.getAdded())
                .thenReturn(ImmutableSet.of(addedAxiom));
        when(axiomDiff.getRemoved())
                .thenReturn(ImmutableSet.of(removedAxiom));

        when(ontologyDiff.getAnnotationDiff())
                .thenReturn(annotationDiff);
        when(annotationDiff.getAdded())
                .thenReturn(ImmutableSet.of(addedAnnotation));
        when(annotationDiff.getRemoved())
                .thenReturn(ImmutableSet.of(removedAnnotation));

        diff2Changes = new OntologyDiff2OntologyChanges();
        ontologyChanges = diff2Changes.getOntologyChangesFromDiff(ontologyDiff);

    }

    @Test
    public void shouldGenerateAddAxioms() {
        assertThat(ontologyChanges, hasItem(AddAxiomChange.of(ontologyId, addedAxiom)));
    }

    @Test
    public void shouldGenerateRemoveAxioms() {
        assertThat(ontologyChanges, hasItem(RemoveAxiomChange.of(ontologyId, removedAxiom)));
    }

    @Test
    public void shouldGenerateAddAnnotations() {
        assertThat(ontologyChanges, hasItem(AddOntologyAnnotationChange.of(ontologyId, addedAnnotation)));
    }

    @Test
    public void shouldGenerateRemvoveAnnotations() {
        assertThat(ontologyChanges, hasItem(RemoveOntologyAnnotationChange.of(ontologyId, removedAnnotation)));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
public void shouldThrowNpeIfOntologyDiffIsNull() {
    assertThrows(NullPointerException.class, () -> { 
        diff2Changes.getOntologyChangesFromDiff(null);
     });
}
}
