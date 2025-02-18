package edu.stanford.protege.webprotege.merge;

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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-20
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AnnotationDiffCalculator_TestCase {


    private AnnotationDiffCalculator calculator;

    @Mock
    private Ontology fromOnt;

    @Mock
    private Ontology toOnt;

    @Mock
    private OWLAnnotation annotationA, annotationB, annotationC;

    @BeforeEach
    public void setUp() {
        calculator = new AnnotationDiffCalculator();
        when(fromOnt.getAnnotations())
                .thenReturn(ImmutableSet.of(annotationA, annotationB));
        when(toOnt.getAnnotations())
                .thenReturn(ImmutableSet.of(annotationB, annotationC));
    }

    @Test
    public void shouldGetAddedAnnotations() {
        var diff = calculator.computeDiff(fromOnt, toOnt);
        var addedAnnotations = diff.getAdded();
        assertThat(addedAnnotations, contains(annotationC));
    }

    @Test
    public void shouldGetRemovedAnnotations() {
        var diff = calculator.computeDiff(fromOnt, toOnt);
        var removedAnnotations = diff.getRemoved();
        assertThat(removedAnnotations, contains(annotationA));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
public void shouldThrowNpeIfFromOntologyIsNull() {
    assertThrows(NullPointerException.class, () -> {
        calculator.computeDiff(null, toOnt);
     });
}

    @SuppressWarnings("ConstantConditions")
    @Test
public void shouldThrowNpeIfToOntologyIsNull() {
    assertThrows(NullPointerException.class, () -> { 
        calculator.computeDiff(fromOnt, null);
     });
}
}
