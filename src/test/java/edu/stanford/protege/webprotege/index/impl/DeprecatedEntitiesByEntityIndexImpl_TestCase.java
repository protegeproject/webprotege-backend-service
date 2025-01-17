package edu.stanford.protege.webprotege.index.impl;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.change.AddAxiomChange;
import edu.stanford.protege.webprotege.index.ProjectOntologiesIndex;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntologyID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-15
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class DeprecatedEntitiesByEntityIndexImpl_TestCase {

    private DeprecatedEntitiesByEntityIndexImpl impl;

    @Mock
    private ProjectOntologiesIndex projectOntologiesIndex;

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private OWLEntity entity;

    @Mock
    private IRI entityIri;

    @Mock
    private OWLAnnotationAssertionAxiom annotationAssertion;

    @BeforeEach
    public void setUp() {
        impl = new DeprecatedEntitiesByEntityIndexImpl(projectOntologiesIndex);

        when(entity.getIRI())
                .thenReturn(entityIri);

        when(annotationAssertion.getSubject())
                .thenReturn(entityIri);
    }

    @Test
    public void shouldGetDependencies() {
        assertThat(impl.getDependencies(), containsInAnyOrder(projectOntologiesIndex));
    }

    @Test
    public void shouldNotFindEntityToBeDeprecated() {
        var deprecated = impl.isDeprecated(entity);
        assertThat(deprecated, Matchers.is(false));
    }

    @Test
    public void shouldFindEntityToBeDeprecated() {
        when(annotationAssertion.isDeprecatedIRIAssertion())
                .thenReturn(true);
        impl.applyChanges(ImmutableList.of(AddAxiomChange.of(ontologyId, annotationAssertion)));
        var deprecated = impl.isDeprecated(entity);
        assertThat(deprecated, Matchers.is(true));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
public void shouldThrowNpeIfEntityIsNull() {
    assertThrows(NullPointerException.class, () -> { 
        impl.isDeprecated(null);
     });
}
}
