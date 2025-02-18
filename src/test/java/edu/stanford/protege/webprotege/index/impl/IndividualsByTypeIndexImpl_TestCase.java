package edu.stanford.protege.webprotege.index.impl;

import edu.stanford.protege.webprotege.hierarchy.ClassHierarchyProvider;
import edu.stanford.protege.webprotege.index.ClassAssertionAxiomsByClassIndex;
import edu.stanford.protege.webprotege.index.ClassAssertionAxiomsByIndividualIndex;
import edu.stanford.protege.webprotege.index.ProjectOntologiesIndex;
import edu.stanford.protege.webprotege.index.ProjectSignatureByTypeIndex;
import edu.stanford.protege.webprotege.individuals.InstanceRetrievalMode;
import edu.stanford.protege.webprotege.shortform.DictionaryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import uk.ac.manchester.cs.owl.owlapi.OWLClassImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLNamedIndividualImpl;

import java.util.Collections;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-19
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class IndividualsByTypeIndexImpl_TestCase {

    private IndividualsByTypeIndexImpl impl;

    @Mock
    private ProjectOntologiesIndex projectOntologiesIndex;

    @Mock
    private ProjectSignatureByTypeIndex projectSignatureIndex;

    @Mock
    private ClassAssertionAxiomsByIndividualIndex classAssertionsByIndividual;

    @Mock
    private ClassAssertionAxiomsByClassIndex classAssertionsByClass;

    @Mock
    private ClassHierarchyProvider classHierarchyProvider;

    @Mock
    private DictionaryManager dictionaryManager;

    @Mock
    private OWLDataFactory dataFactory;

    private OWLClass owlThing = new OWLClassImpl(OWLRDFVocabulary.OWL_THING.getIRI());

    @Mock
    private OWLOntologyID ontologyId;

    private OWLNamedIndividual
            indA = new OWLNamedIndividualImpl(mock(IRI.class)),
            indB = new OWLNamedIndividualImpl(mock(IRI.class));

    @Mock
    private OWLClassAssertionAxiom indATypeClsA;

    private OWLClass
            clsA = new OWLClassImpl(mock(IRI.class)),
            clsB = new OWLClassImpl(mock(IRI.class));


    @BeforeEach
    public void setUp() {
        impl = new IndividualsByTypeIndexImpl(projectOntologiesIndex,
                                              projectSignatureIndex,
                                              classAssertionsByIndividual,
                                              classAssertionsByClass,
                                              classHierarchyProvider,
                                              dictionaryManager,
                                              dataFactory);

        // SubClassOf(:clsA, :clsB)
        // ClassAssertion(:clsA :indA)

        when(dataFactory.getOWLThing()).thenReturn(owlThing);

        when(projectOntologiesIndex.getOntologyIds())
                .thenAnswer(invocation -> Stream.of(ontologyId));

        when(projectSignatureIndex.getSignature(EntityType.NAMED_INDIVIDUAL))
                .thenAnswer(invocation -> Stream.of(indA, indB));

        when(indATypeClsA.getClassExpression())
                .thenReturn(clsA);
        when(indATypeClsA.getIndividual())
                .thenReturn(indA);

        when(classAssertionsByIndividual.getClassAssertionAxioms(any(), any()))
                .thenAnswer(invocation -> Stream.empty());
        when(classAssertionsByIndividual.getClassAssertionAxioms(indA, ontologyId))
                .thenAnswer(invocation -> Stream.of(indATypeClsA));

        when(classAssertionsByClass.getClassAssertionAxioms(any(), any()))
                .thenAnswer(invocation -> Stream.empty());
        when(classAssertionsByClass.getClassAssertionAxioms(clsA, ontologyId))
                .thenAnswer(invocation -> Stream.of(indATypeClsA));



        when(classHierarchyProvider.getDescendants(clsB))
                .thenAnswer(invocation -> Collections.singleton(clsA));
    }

    @Test
    public void shouldGetDependencies() {
        assertThat(impl.getDependencies(), containsInAnyOrder(projectOntologiesIndex,
                                                              projectSignatureIndex,
                                                              classAssertionsByIndividual,
                                                              classAssertionsByClass));
    }

    @Test
    public void shouldGetUntypedIndividualsAsDirectInstancesOfOwlThing() {
        var inds = impl.getIndividualsByType(owlThing, InstanceRetrievalMode.DIRECT_INSTANCES).collect(toSet());
        assertThat(inds, contains(indB));
    }

    @Test
    public void shouldAllIndividualsAsIndirectInstancesOfOwlThing() {
        var inds = impl.getIndividualsByType(owlThing, InstanceRetrievalMode.ALL_INSTANCES).collect(toSet());
        assertThat(inds, containsInAnyOrder(indA, indB));
    }

    @Test
    public void shouldGetDirectAssertedInstancesOfClsA() {
        var inds = impl.getIndividualsByType(clsA, InstanceRetrievalMode.DIRECT_INSTANCES).collect(toSet());
        assertThat(inds, containsInAnyOrder(indA));
    }

    @Test
    public void shouldGetInirectAssertedInstancesOfClsA() {
        var inds = impl.getIndividualsByType(clsA, InstanceRetrievalMode.ALL_INSTANCES).collect(toSet());
        assertThat(inds, containsInAnyOrder(indA));
    }

    @Test
    public void shouldGetDirectAssertedInstancesOfClsB() {
        var inds = impl.getIndividualsByType(clsB, InstanceRetrievalMode.DIRECT_INSTANCES).collect(toSet());
        assertThat(inds.isEmpty(), is(true));
    }

    @Test
    public void shouldGetIndirectAssertedInstancesOfClsB() {
        var inds = impl.getIndividualsByType(clsB, InstanceRetrievalMode.ALL_INSTANCES).collect(toSet());
        assertThat(inds, contains(indA));
    }
}
