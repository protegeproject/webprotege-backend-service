package edu.stanford.protege.webprotege.index.impl;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.change.AddAxiomChange;
import edu.stanford.protege.webprotege.index.ProjectOntologiesIndex;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntologyID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-15
 */
@RunWith(MockitoJUnitRunner.class)
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

    @Before
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
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeIfEntityIsNull() {
        impl.isDeprecated(null);
    }
}
