package edu.stanford.protege.webprotege.index.impl;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.change.AddAxiomChange;
import edu.stanford.protege.webprotege.change.RemoveAxiomChange;
import edu.stanford.protege.webprotege.index.AxiomsByTypeIndex;
import edu.stanford.protege.webprotege.index.ProjectOntologiesIndex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.semanticweb.owlapi.model.*;
import uk.ac.manchester.cs.owl.owlapi.OWLAnnotationPropertyDomainAxiomImpl;

import java.util.Collections;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-08
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AnnotationAxiomsByIriReferenceIndexImpl_AnnotationPropertyDomain_TestCase {

    private AnnotationAxiomsByIriReferenceIndexImpl impl;

    @Mock
    private ProjectOntologiesIndex projectOntologiesIndex;

    @Mock
    private AxiomsByTypeIndex axiomsByTypeIndex;

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private IRI domainIri, otherDomainIri;

    @Mock
    private OWLAnnotation axiomAnnotation;

    @Mock
    private IRI axiomAnnotationValue;

    @Mock
    private OWLAnnotationProperty property;

    private OWLAnnotationPropertyDomainAxiom annotationPropertyDomainAxiom, otherAnnotationPropertyDomainAxiom;


    @BeforeEach
    public void setUp() {
        annotationPropertyDomainAxiom = new OWLAnnotationPropertyDomainAxiomImpl(property, domainIri, axiomAnnotations());
        otherAnnotationPropertyDomainAxiom = new OWLAnnotationPropertyDomainAxiomImpl(property, otherDomainIri, axiomAnnotations());
        impl = new AnnotationAxiomsByIriReferenceIndexImpl();
        impl.applyChanges(ImmutableList.of(AddAxiomChange.of(ontologyId, annotationPropertyDomainAxiom)));
    }

    private Set<OWLAnnotation> axiomAnnotations() {
        when(axiomAnnotation.getValue())
                .thenReturn(axiomAnnotationValue);
        return Collections.singleton(axiomAnnotation);
    }

    @Test
    public void shouldGetAnnotationPropertyDomainAxiomByDomainIri() {
        var axioms = impl.getReferencingAxioms(domainIri, ontologyId).collect(toSet());
        assertThat(axioms, hasItem(annotationPropertyDomainAxiom));
    }

    @Test
    public void shouldGetAnnotationPropertyDomainAxiomByAxiomAnnotationValue() {
        var axioms = impl.getReferencingAxioms(axiomAnnotationValue, ontologyId).collect(toSet());
        assertThat(axioms, hasItem(annotationPropertyDomainAxiom));
    }


    @Test
    public void shouldHandleAddAnnotationPropertyDomainAxiom() {
        var changeRecord = AddAxiomChange.of(ontologyId, otherAnnotationPropertyDomainAxiom);

        impl.applyChanges(ImmutableList.of(changeRecord));

        var axioms = impl.getReferencingAxioms(otherDomainIri, ontologyId).collect(toSet());
        assertThat(axioms, hasItems(otherAnnotationPropertyDomainAxiom));
    }

    @Test
    public void shouldHandleRemoveAnnotationPropertyDomainAxiom() {
        var changeRecord = RemoveAxiomChange.of(ontologyId, annotationPropertyDomainAxiom);

        impl.applyChanges(ImmutableList.of(changeRecord));

        var axioms = impl.getReferencingAxioms(domainIri, ontologyId).collect(toSet());
        assertThat(axioms.isEmpty(), is(true));
    }
}
