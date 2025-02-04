package edu.stanford.protege.webprotege.index.impl;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.change.AddOntologyAnnotationChange;
import edu.stanford.protege.webprotege.change.RemoveOntologyAnnotationChange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.semanticweb.owlapi.model.*;

import java.util.Collections;

import static java.util.stream.Collectors.toSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.Annotation;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.AnnotationProperty;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-06
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class OntologyAnnotationsIndexImpl_TestCase {

    private OntologyAnnotationsIndexImpl impl;

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private OWLAnnotationValue annotationValue;

    private OWLAnnotation ontologyAnnotation;

    private OWLAnnotation annotationAnnotation;

    private OWLAnnotationProperty property, otherProperty;

    @BeforeEach
    public void setUp() {
        property = AnnotationProperty(mock(IRI.class));
        otherProperty = AnnotationProperty(mock(IRI.class));
        annotationAnnotation = Annotation(otherProperty, annotationValue);
        ontologyAnnotation = Annotation(property,
                                        annotationValue)
                .getAnnotatedAnnotation(Collections.singleton(annotationAnnotation));
        impl = new OntologyAnnotationsIndexImpl();
        impl.applyChanges(ImmutableList.of(AddOntologyAnnotationChange.of(ontologyId, ontologyAnnotation)));
    }

    @Test
    public void shouldContainAnnotation() {
        assertThat(impl.containsAnnotation(ontologyAnnotation, ontologyId), is(true));
    }

    @Test
    public void shouldContainEntityInSignature() {
        assertThat(impl.containsEntityInOntologyAnnotationsSignature(property, ontologyId), is(true));
    }

    @Test
    public void shouldContainAnnotationOnAnnotationInSignature() {
        assertThat(impl.containsEntityInOntologyAnnotationsSignature(otherProperty, ontologyId), is(true));
    }

    @Test
    public void shouldGetEmptyStreamForUnknownOntology() {
        var ontologyAnnotationsStream = impl.getOntologyAnnotations(mock(OWLOntologyID.class));
        assertThat(ontologyAnnotationsStream.count(), is(0L));
    }

    @Test
    public void shouldGetOntologyAnnotations() {
        var ontologyAnnotationsStream = impl.getOntologyAnnotations(ontologyId);
        var ontologyAnnotations = ontologyAnnotationsStream.collect(toSet());
        assertThat(ontologyAnnotations, contains(ontologyAnnotation));
    }

    @Test
    public void shouldGetOntologyAnnotationsSignature() {
        var signature = impl.getOntologyAnnotationsSignature(ontologyId)
                            .collect(toSet());
        assertThat(signature, containsInAnyOrder(property, otherProperty));
    }

    @Test
    public void shouldNotContainEntityInSignature() {
        assertThat(impl.containsEntityInOntologyAnnotationsSignature(mock(OWLEntity.class), ontologyId), is(false));
    }

    @Test
    public void shouldRemoveAnnotation() {
        impl.applyChanges(ImmutableList.of(RemoveOntologyAnnotationChange.of(ontologyId, ontologyAnnotation)));
        assertThat(impl.getOntologyAnnotations(ontologyId).count(), is(0L));
    }
}
