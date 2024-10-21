package edu.stanford.protege.webprotege.hierarchy;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.change.AddAxiomChange;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.index.*;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.io.ToStringRenderer;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.ShortFormProvider;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FixedRootClassHierarchyProvider_TestCase {

    private ClassHierarchyProviderImpl classHierarchyProvider;

    private OWLDataFactory dataFactory = new OWLDataFactoryImpl();

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

    private OWLClass owlThing, clsA, clsA2, clsB, clsC, clsD, clsE, clsX_FixedRoot, clsF;

    private OWLSubClassOfAxiom clsASubClassOfClsB, clsBSubClassOfClsC, clsCSubClassOfClsX, clsFSubClassOfClsC, getClsFSubClassOfClsC;

    private OWLEquivalentClassesAxiom clsA2EquivalentToClsDandClsE;

    @Mock
    private IRI clsAIri, clsA2Iri, clsBIri, clsCIri, clsDIri, clsEIri, clsXIri, clsFIri;

    @Mock
    private ClassHierarchyChildrenAxiomsIndex classHierarchyChildrenAxiomsIndex;

    @Before
    public void setUp() {
        ToStringRenderer.getInstance().setRenderer(new OWLObjectRenderer() {
            @Override
            public void setShortFormProvider(@NotNull ShortFormProvider shortFormProvider) {

            }

            @NotNull
            @Override
            public String render(@NotNull OWLObject owlObject) {
                return ((OWLClass) owlObject).getIRI().toString();
            }
        });
        when(projectOntologiesIndex.getOntologyIds())
                .thenAnswer(invocation -> Stream.of(ontologyId));

        owlThing = dataFactory.getOWLThing();

        clsA = dataFactory.getOWLClass(clsAIri);
        clsA2 = dataFactory.getOWLClass(clsA2Iri);
        clsB = dataFactory.getOWLClass(clsBIri);
        clsC = dataFactory.getOWLClass(clsCIri);
        clsD = dataFactory.getOWLClass(clsDIri);
        clsE = dataFactory.getOWLClass(clsEIri);
        clsF = dataFactory.getOWLClass(clsFIri);
        clsX_FixedRoot = dataFactory.getOWLClass(clsXIri);

        // A -> B -> C -> X
        // A2 -> {C, D}
        // D -> {}
        // E -> {}
        // -----
        // + F -> C
        clsASubClassOfClsB = dataFactory.getOWLSubClassOfAxiom(clsA, clsB);
        clsBSubClassOfClsC = dataFactory.getOWLSubClassOfAxiom(clsB, clsC);
        clsCSubClassOfClsX = dataFactory.getOWLSubClassOfAxiom(clsC, clsX_FixedRoot);
        clsFSubClassOfClsC = dataFactory.getOWLSubClassOfAxiom(clsF, clsC);

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
        when(subClassOfAxiomsBySubClassIndex.getSubClassOfAxiomsForSubClass(clsC, ontologyId))
                .thenAnswer(invocation -> ImmutableList.of(clsCSubClassOfClsX).stream());

        when(equivalentClassesAxiomIndex.getEquivalentClassesAxioms(clsA2, ontologyId))
                .thenAnswer(invocation -> Stream.of(clsA2EquivalentToClsDandClsE));

        when(entitiesInProjectSignatureByIriIndex.getEntitiesInSignature(clsAIri))
                .thenAnswer(invocation -> Stream.of(clsA));
        when(entitiesInProjectSignatureByIriIndex.getEntitiesInSignature(clsBIri))
                .thenAnswer(invocation -> Stream.of(clsB));
        when(entitiesInProjectSignatureByIriIndex.getEntitiesInSignature(clsCIri))
                .thenAnswer(invocation -> Stream.of(clsC));
        when(entitiesInProjectSignatureByIriIndex.getEntitiesInSignature(clsDIri))
                .thenAnswer(invocation -> Stream.of(clsD));
        when(entitiesInProjectSignatureByIriIndex.getEntitiesInSignature(clsEIri))
                .thenAnswer(invocation -> Stream.of(clsE));

        when(projectSignatureByTypeIndex.getSignature(EntityType.CLASS))
                .thenReturn(Stream.of(clsA, clsA2, clsB, clsC, clsD, clsE, clsF, clsX_FixedRoot));

        when(classHierarchyChildrenAxiomsIndex.getChildrenAxioms(clsB))
                .thenAnswer(invocation -> Stream.of(clsASubClassOfClsB));

        when(classHierarchyChildrenAxiomsIndex.getChildrenAxioms(clsC))
                .thenAnswer(invocation -> Stream.of(clsBSubClassOfClsC));

        when(classHierarchyChildrenAxiomsIndex.getChildrenAxioms(clsX_FixedRoot))
                .thenAnswer(invocation -> Stream.of(clsCSubClassOfClsX));

        when(classHierarchyChildrenAxiomsIndex.getChildrenAxioms(clsD))
                .thenAnswer(invocation -> Stream.of(clsA2EquivalentToClsDandClsE));



        classHierarchyProvider = new ClassHierarchyProviderImpl(projectId,
                Set.of(clsX_FixedRoot),
                projectOntologiesIndex,
                subClassOfAxiomsBySubClassIndex,
                equivalentClassesAxiomIndex,
                projectSignatureByTypeIndex,
                entitiesInProjectSignatureByIriIndex,
                classHierarchyChildrenAxiomsIndex);
    }

    @Test
    public void shouldGetRoots() {
        var roots = classHierarchyProvider.getRoots();
        assertThat(roots, containsInAnyOrder(clsX_FixedRoot));
        assertThat(roots, hasSize(1));
    }

    @Test
    public void shouldGetChilrenOfRoot() {
        var children = classHierarchyProvider.getChildren(clsX_FixedRoot);
        assertThat(children, containsInAnyOrder(clsC));
        assertThat(children, hasSize(1));
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
        assertThat(ancestors, containsInAnyOrder(clsB, clsC, clsX_FixedRoot));
        assertThat(ancestors, not(containsInAnyOrder(owlThing)));
    }

    @Test
    public void shouldGetRootAsOrphanParents() {
        var parents = classHierarchyProvider.getParents(clsE);
        assertThat(parents, hasSize(0));
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

    @Test
    public void shouldNotGetSubclassesOfThing() {
        var children = classHierarchyProvider.getChildren(owlThing);
        assertThat(children, hasSize(0));
    }

    @Test
    public void shouldGetParentsOfRoot() {
        var parents = classHierarchyProvider.getParents(clsX_FixedRoot);
        assertThat(parents, hasSize(0));
    }

    @Test
    public void shouldNotContainImplicitRootsOfThing() {
        var containsD = classHierarchyProvider.containsReference(clsD);
        assertThat(containsD, is(false));

        var containsE = classHierarchyProvider.containsReference(clsE);
        assertThat(containsE, is(false));
    }

    @Test
    public void shouldContainRootsAndDescendants() {
        var containsRoot = classHierarchyProvider.containsReference(clsX_FixedRoot);
        assertThat(containsRoot, is(true));

        var containsA = classHierarchyProvider.containsReference(clsA);
        assertThat(containsA, is(true));
        var containsB = classHierarchyProvider.containsReference(clsB);
        assertThat(containsB, is(true));
        var containsC = classHierarchyProvider.containsReference(clsC);
        assertThat(containsC, is(true));
    }

    @Test
    public void shouldHandleChanges() {
        // Add F

        when(subClassOfAxiomsBySubClassIndex.getSubClassOfAxiomsForSubClass(clsF, ontologyId))
                .thenAnswer(invocation -> ImmutableList.of(clsFSubClassOfClsC).stream());

        when(entitiesInProjectSignatureByIriIndex.getEntitiesInSignature(clsFIri))
                .thenAnswer(invocation -> Stream.of(clsF));

        when(projectSignatureByTypeIndex.getSignature(EntityType.CLASS))
                .thenReturn(Stream.of(clsA, clsA2, clsB, clsC, clsD, clsE, clsX_FixedRoot, clsF));

        when(classHierarchyChildrenAxiomsIndex.getChildrenAxioms(clsC))
                .thenAnswer(invocation -> Stream.of(clsBSubClassOfClsC, clsFSubClassOfClsC));

        when(classHierarchyChildrenAxiomsIndex.getChildrenAxioms(clsF))
                .thenAnswer(invocation -> Stream.of());

        when(entitiesInProjectSignatureByIriIndex.getEntitiesInSignature(clsFIri))
                .thenAnswer(invocation -> Stream.of(clsF));

        classHierarchyProvider.handleChanges(List.of(new AddAxiomChange(ontologyId, clsFSubClassOfClsC)));

        assertThat(classHierarchyProvider.containsReference(clsF), is(true));
        assertThat(classHierarchyProvider.getRoots(), not(contains(clsF)));
        assertThat(classHierarchyProvider.getChildren(clsC).contains(clsF), is(true));
        assertThat(classHierarchyProvider.getParents(clsF), contains(clsC));
        assertThat(classHierarchyProvider.getChildren(clsF), hasSize(0));

    }
}
