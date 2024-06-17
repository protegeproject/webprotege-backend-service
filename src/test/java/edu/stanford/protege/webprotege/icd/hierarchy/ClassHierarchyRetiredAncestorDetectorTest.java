package edu.stanford.protege.webprotege.icd.hierarchy;

import edu.stanford.protege.webprotege.hierarchy.ClassHierarchyProvider;
import edu.stanford.protege.webprotege.icd.RetiredClassChecker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.semanticweb.owlapi.model.OWLClass;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClassHierarchyRetiredAncestorDetectorTest {

    @Mock
    private ClassHierarchyProvider classHierarchyProvider;

    @Mock
    private RetiredClassChecker retiredClassesIndex;

    @Mock
    private OWLClass clsA, clsA1, clsB, clsC, clsD, clsE;

    private ClassHierarchyRetiredAncestorDetector classRetiredAncestorDetector;


    @BeforeEach
    public void setUp() {
        classRetiredAncestorDetector = new ClassHierarchyRetiredAncestorDetectorImpl(classHierarchyProvider, retiredClassesIndex);
    }

    @Test
    void givenClassesWithNoRetiredAncestors_whenCallingHasRetired_thenGetFalse() {
        when(classHierarchyProvider.getAncestors(clsA1)).thenReturn(Set.of(clsA));
        when(retiredClassesIndex.isRetired(clsA)).thenReturn(false);
        assertFalse(classRetiredAncestorDetector.hasRetiredAncestor(clsA1));

        verify(retiredClassesIndex).isRetired(any());
        verify(classHierarchyProvider).getAncestors(any());
    }

    @Test
    void givenOwlClasses_whenClassHasRetiredAncestor_thenReturnTheRetiredAncestorClass() {
        when(classHierarchyProvider.getAncestors(clsD)).thenReturn(Set.of(clsA, clsB));
        when(retiredClassesIndex.isRetired(clsA)).thenReturn(true);
        when(retiredClassesIndex.isRetired(clsB)).thenReturn(false);
        var classesWithRetiredAncestors = classRetiredAncestorDetector.getRetiredAncestors(clsD);
        assertTrue(classesWithRetiredAncestors.contains(clsA));

        verify(retiredClassesIndex, times(2)).isRetired(any());
        verify(classHierarchyProvider).getAncestors(any());
    }

    @Test
    void givenSetOfOwlClasses_whenAClassHasRetiredAncestor_thenReturnThatClass() {
        var setWithClasses = Set.of(clsD, clsE, clsC);

        when(classHierarchyProvider.getAncestors(clsE)).thenReturn(Set.of(clsB));
        when(retiredClassesIndex.isRetired(clsB)).thenReturn(false);

        when(classHierarchyProvider.getAncestors(clsD)).thenReturn(Set.of(clsA));
        when(retiredClassesIndex.isRetired(clsA)).thenReturn(true);

        when(classHierarchyProvider.getAncestors(clsC)).thenReturn(Set.of());


        var classesWithRetiredAncestors = classRetiredAncestorDetector.getClassesWithRetiredAncestors(setWithClasses);
        assertTrue(classesWithRetiredAncestors.contains(clsD));

        verify(retiredClassesIndex, atMost(2)).isRetired(any());
        verify(classHierarchyProvider, times(3)).getAncestors(any());
    }
}