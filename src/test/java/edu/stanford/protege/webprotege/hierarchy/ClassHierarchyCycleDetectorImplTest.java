package edu.stanford.protege.webprotege.hierarchy;

import edu.stanford.protege.webprotege.change.AddAxiomChange;
import edu.stanford.protege.webprotege.change.OntologyChange;
import edu.stanford.protege.webprotege.change.OntologyChangeList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.*;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class ClassHierarchyCycleDetectorImplTest {

    private ClassHierarchyCycleDetector classHierarchyCycleDetector;

    private final OWLDataFactory dataFactory = new OWLDataFactoryImpl();

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private OWLClass clsA, clsB, clsC;

    private OWLSubClassOfAxiom clsASubClassOfClsB, clsBSubClassOfClsC, clsBSubClassOfClsA, clsASubClassOfClsC;

    @Mock
    private IRI clsAIri, clsBIri, clsCIri;

    @Mock
    private ClassHierarchyProviderImpl classHierarchyProvider;

    @Before
    public void setUp() {

        clsA = dataFactory.getOWLClass(clsAIri);
        clsB = dataFactory.getOWLClass(clsBIri);
        clsC = dataFactory.getOWLClass(clsCIri);

        clsASubClassOfClsB = dataFactory.getOWLSubClassOfAxiom(clsA, clsB);
        clsBSubClassOfClsC = dataFactory.getOWLSubClassOfAxiom(clsB, clsC);
        clsBSubClassOfClsA = dataFactory.getOWLSubClassOfAxiom(clsB, clsA);
        clsASubClassOfClsC = dataFactory.getOWLSubClassOfAxiom(clsA, clsC);

        classHierarchyCycleDetector = new ClassHierarchyCycleDetectorImpl(classHierarchyProvider);

    }

    @Test
    public void givenHierarchyWhenAddingAxiomThatCreatesCycleThenGetTrueResponseForCycleCheck() {
        OntologyChangeList.Builder<Boolean> changesListBuilder = new OntologyChangeList.Builder<>();
        changesListBuilder.add(AddAxiomChange.of(checkNotNull(ontologyId), checkNotNull(clsBSubClassOfClsA)));
        changesListBuilder.add(AddAxiomChange.of(checkNotNull(ontologyId), checkNotNull(clsASubClassOfClsC)));

        List<OntologyChange> ontologyChangeList = changesListBuilder.build(true).getChanges();

        when(classHierarchyProvider.filterIrrelevantChanges(ontologyChangeList)).thenReturn(ontologyChangeList);

        when(classHierarchyProvider.isAncestor(any(), any())).thenReturn(true);
        var hasCycle = classHierarchyCycleDetector.hasCycle(ontologyChangeList);
        assertTrue(hasCycle);
        verify(classHierarchyProvider, times(1)).isAncestor(any(), any());
    }

    @Test
    public void givenHierarchyWhenAddingAxiomThatCreatesCycleThenGetResponseWithClassesWithCycles() {
        OntologyChangeList.Builder<Boolean> changesListBuilder = new OntologyChangeList.Builder<>();
        changesListBuilder.add(AddAxiomChange.of(checkNotNull(ontologyId), checkNotNull(clsBSubClassOfClsA)));
        changesListBuilder.add(AddAxiomChange.of(checkNotNull(ontologyId), checkNotNull(clsASubClassOfClsC)));

        List<OntologyChange> ontologyChangeList = changesListBuilder.build(true).getChanges();

        when(classHierarchyProvider.filterIrrelevantChanges(ontologyChangeList)).thenReturn(ontologyChangeList);

        when(classHierarchyProvider.isAncestor(any(), any())).thenReturn(true);
        var classesWithCycle = classHierarchyCycleDetector.getClassesWithCycle(ontologyChangeList);
        assertEquals(3, classesWithCycle.size());
        verify(classHierarchyProvider, times(3)).isAncestor(any(), any());
    }
}