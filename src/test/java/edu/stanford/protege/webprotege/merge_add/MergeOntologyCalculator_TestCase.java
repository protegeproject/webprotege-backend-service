package edu.stanford.protege.webprotege.merge_add;

import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.project.Ontology;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class MergeOntologyCalculator_TestCase {

    private MergeOntologyCalculator mergeCalculator;

    private Collection<Ontology> projectOntologies;

    private Collection<Ontology> uploadedOntologies;

    private List<OWLOntologyID> selectedOntologies;

    @Mock
    private Ontology ontologyA, ontologyB, ontologyC, ontologyD, ontologyE;

    @Mock
    private OWLAnnotation annotationA, annotationB, annotationC, annotationD, annotationE, annotationF;

    @Mock
    private OWLAxiom axiomA, axiomB, axiomC, axiomD, axiomE, axiomF;

    @Mock
    private OWLOntologyID ontologyIDA, ontologyIDB, ontologyIDC, ontologyIDD, ontologyIDE;

    @BeforeEach
    public void setUp() {
        mergeCalculator = new MergeOntologyCalculator();

        projectOntologies = new ArrayList<>();
        projectOntologies.add(ontologyA);
        projectOntologies.add(ontologyB);
        projectOntologies.add(ontologyC);

        uploadedOntologies = new ArrayList<>();
        uploadedOntologies.add(ontologyD);
        uploadedOntologies.add(ontologyE);

        selectedOntologies = new ArrayList<>();
        selectedOntologies.add(ontologyIDA);
        selectedOntologies.add(ontologyIDD);

        when(ontologyA.getAnnotations())
                .thenReturn(ImmutableSet.of(annotationA, annotationB));
        when(ontologyB.getAnnotations())
                .thenReturn(ImmutableSet.of(annotationC, annotationD));
        when(ontologyC.getAnnotations())
                .thenReturn(ImmutableSet.of(annotationE, annotationF));
        when(ontologyD.getAnnotations())
                .thenReturn(ImmutableSet.of(annotationB, annotationC));
        when(ontologyE.getAnnotations())
                .thenReturn(ImmutableSet.of(annotationD, annotationE));


        when(ontologyA.getAxioms())
                .thenReturn(ImmutableSet.of(axiomA, axiomB));
        when(ontologyB.getAxioms())
                .thenReturn(ImmutableSet.of(axiomC, axiomD));
        when(ontologyC.getAxioms())
                .thenReturn(ImmutableSet.of(axiomE, axiomF));
        when(ontologyD.getAxioms())
                .thenReturn(ImmutableSet.of(axiomB, axiomC));
        when(ontologyE.getAxioms())
                .thenReturn(ImmutableSet.of(axiomD, axiomE));

        when(ontologyA.getOntologyId()).thenReturn(ontologyIDA);
        when(ontologyB.getOntologyId()).thenReturn(ontologyIDB);
        when(ontologyC.getOntologyId()).thenReturn(ontologyIDC);
        when(ontologyD.getOntologyId()).thenReturn(ontologyIDD);
        when(ontologyE.getOntologyId()).thenReturn(ontologyIDE);

    }

    @Test
    public void shouldGetMergeAxioms(){
        var axioms = mergeCalculator.getMergeAxioms(projectOntologies, uploadedOntologies, selectedOntologies);
        assertThat(axioms, contains(axiomA, axiomB, axiomC));
    }

    @Test
    public void shouldGetMergeAnnotations(){
        var annotations = mergeCalculator.getMergeAnnotations(projectOntologies, uploadedOntologies, selectedOntologies);
        assertThat(annotations, contains(annotationA, annotationB, annotationC));
    }
}
