package edu.stanford.protege.webprotege.hierarchy;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.MockingUtils;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.index.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.*;
import org.mockito.quality.Strictness;
import org.semanticweb.owlapi.model.*;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-16
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ClassHierarchyProviderImpl_TestCase {

    private ClassHierarchyProviderImpl classHierarchyProvider;

    private OWLDataFactory dataFactory = new OWLDataFactoryImpl();

    private OWLClass owlThing = dataFactory.getOWLThing();

    private ProjectId projectId = ProjectId.generate();

    @Mock
    private ProjectOntologiesIndex projectOntologiesIndex;

    @Mock
    private SubClassOfAxiomsBySubClassIndex subClassOfAxiomsBySubClassIndex;

    @Mock
    private EquivalentClassesAxiomsIndex equivalentClassesAxiomIndex;

    @Mock
    private ProjectSignatureByTypeIndex projectSignatureByTypeIndex;

    @Mock
    private EntitiesInProjectSignatureByIriIndex entitiesInProjectSignatureByIriIndex;

    @Mock
    private OWLOntologyID ontologyId;

    private OWLClass clsA, clsA2, clsB, clsC, clsD, clsE;

    private OWLSubClassOfAxiom clsASubClassOfClsB, clsBSubClassOfClsC;

    private OWLEquivalentClassesAxiom clsA2EquivalentToClsDandClsE;

    private IRI clsAIri, clsA2Iri, clsBIri, clsCIri, clsDIri, clsEIri;

    @Mock
    private ClassHierarchyChildrenAxiomsIndex classHierarchyChildrenAxiomsIndex;

    @BeforeEach
    public void setUp() {
        clsAIri = MockingUtils.mockIRI();
        clsA2Iri = MockingUtils.mockIRI();
        clsBIri = MockingUtils.mockIRI();
        clsCIri = MockingUtils.mockIRI();
        clsDIri = MockingUtils.mockIRI();
        clsEIri = MockingUtils.mockIRI();


        when(projectOntologiesIndex.getOntologyIds())
                .thenAnswer(invocation -> Stream.of(ontologyId));

        clsA = dataFactory.getOWLClass(clsAIri);
        clsA2 = dataFactory.getOWLClass(clsA2Iri);
        clsB = dataFactory.getOWLClass(clsBIri);
        clsC = dataFactory.getOWLClass(clsCIri);
        clsD = dataFactory.getOWLClass(clsDIri);
        clsE = dataFactory.getOWLClass(clsEIri);

        clsASubClassOfClsB = dataFactory.getOWLSubClassOfAxiom(clsA, clsB);
        clsBSubClassOfClsC = dataFactory.getOWLSubClassOfAxiom(clsB, clsC);

        clsA2EquivalentToClsDandClsE = dataFactory.getOWLEquivalentClassesAxiom(
                clsA2,
                dataFactory.getOWLObjectIntersectionOf(clsD, clsE)
        );

        when(subClassOfAxiomsBySubClassIndex.getSubClassOfAxiomsForSubClass(any(), any()))
                .thenAnswer(invocation -> Stream.empty());
        when(subClassOfAxiomsBySubClassIndex.getSubClassOfAxiomsForSubClass(clsA, ontologyId))
                .thenAnswer(invocation -> ImmutableList.of(clsASubClassOfClsB).stream());
        when(subClassOfAxiomsBySubClassIndex.getSubClassOfAxiomsForSubClass(clsB, ontologyId))
                .thenAnswer(invocation -> ImmutableList.of(clsBSubClassOfClsC).stream());

        when(equivalentClassesAxiomIndex.getEquivalentClassesAxioms(clsA2, ontologyId))
                .thenAnswer(invocation -> Stream.of(clsA2EquivalentToClsDandClsE));

        when(projectSignatureByTypeIndex.getSignature(EntityType.CLASS))
                .thenReturn(Stream.of(clsA, clsA2, clsB, clsC, clsD, clsE));

        when(classHierarchyChildrenAxiomsIndex.getChildrenAxioms(clsB))
                .thenAnswer(invocation -> Stream.of(clsASubClassOfClsB));

        when(classHierarchyChildrenAxiomsIndex.getChildrenAxioms(clsC))
                .thenAnswer(invocation -> Stream.of(clsBSubClassOfClsC));

        when(classHierarchyChildrenAxiomsIndex.getChildrenAxioms(clsD))
                .thenAnswer(invocation -> Stream.of(clsA2EquivalentToClsDandClsE));



        classHierarchyProvider = new ClassHierarchyProviderImpl(projectId,
                                                                owlThing,
                                                                projectOntologiesIndex,
                                                                subClassOfAxiomsBySubClassIndex,
                                                                equivalentClassesAxiomIndex,
                                                                projectSignatureByTypeIndex,
                                                                entitiesInProjectSignatureByIriIndex,
                                                                classHierarchyChildrenAxiomsIndex);

    }

    @Test
    public void shouldGetAssertedSuperClassesAsParents() {
        var parents = classHierarchyProvider.getParents(clsA);
        assertThat(parents, containsInAnyOrder(clsB));
    }

    @Test
    public void shouldGetConjunctsOfEquivalentIntersectionsAsParents() {
        var parents = classHierarchyProvider.getParents(clsA2);
        assertThat(parents, containsInAnyOrder(clsD, clsE));
    }

    @Test
    public void shouldGetAssertedSuperClassesAsAncestors() {
        var ancestors = classHierarchyProvider.getAncestors(clsA);
        assertThat(ancestors, containsInAnyOrder(clsB, clsC, owlThing));
    }

    @Test
    public void shouldGetRootAsOrphanParents() {
        var parents = classHierarchyProvider.getParents(clsE);
        assertThat(parents, containsInAnyOrder(owlThing));
    }

    @Test
    public void shouldGetAssertedSubClassesAsChildren() {
        var children = classHierarchyProvider.getChildren(clsC);
        assertThat(children, containsInAnyOrder(clsB));
    }

    @Test
    public void shouldGetAssertedSubClassesAsDescendants() {
        var children = classHierarchyProvider.getDescendants(clsC);
        assertThat(children, containsInAnyOrder(clsB, clsA));
    }

    @Test
    public void shouldGetEquivalentClassIntersectionConjunctAsChildren() {
        var children = classHierarchyProvider.getChildren(clsD);
        assertThat(children, containsInAnyOrder(clsA2));
    }
}
