package edu.stanford.protege.webprotege.hierarchy;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.bulkop.ChangeEntityParentsActionHandler;
import edu.stanford.protege.webprotege.change.*;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.index.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.*;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class ClassHierarchyCycleDetectorImplTest {

    private ClassHierarchyCycleDetector classHierarchyCycleDetector;

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

    @Mock
    private ClassHierarchyChildrenAxiomsIndex classHierarchyChildrenAxiomsIndex;

    private OWLClass clsA, clsA2, clsB, clsC, clsD, clsE;

    private OWLSubClassOfAxiom clsASubClassOfClsB, clsBSubClassOfClsC, clsBSubClassOfClsA;

    @Mock
    private IRI clsAIri, clsA2Iri, clsBIri, clsCIri, clsDIri, clsEIri;

    @Before
    public void setUp() {
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
        clsBSubClassOfClsA = dataFactory.getOWLSubClassOfAxiom(clsB, clsA);

        when(subClassOfAxiomsBySubClassIndex.getSubClassOfAxiomsForSubClass(any(), any()))
                .thenAnswer(invocation -> Stream.empty());
        when(subClassOfAxiomsBySubClassIndex.getSubClassOfAxiomsForSubClass(clsA, ontologyId))
                .thenAnswer(invocation -> ImmutableList.of(clsASubClassOfClsB).stream());
        when(subClassOfAxiomsBySubClassIndex.getSubClassOfAxiomsForSubClass(clsB, ontologyId))
                .thenAnswer(invocation -> ImmutableList.of(clsBSubClassOfClsC).stream());


        when(projectSignatureByTypeIndex.getSignature(EntityType.CLASS))
                .thenReturn(Stream.of(clsA, clsA2, clsB, clsC, clsD, clsE));


        classHierarchyProvider = new ClassHierarchyProviderImpl(projectId,
                owlThing,
                projectOntologiesIndex,
                subClassOfAxiomsBySubClassIndex,
                equivalentClassesAxiomIndex,
                projectSignatureByTypeIndex,
                entitiesInProjectSignatureByIriIndex,
                classHierarchyChildrenAxiomsIndex);

        classHierarchyCycleDetector = new ClassHierarchyCycleDetectorImpl(classHierarchyProvider);

    }

    @Test
    public void givenHierarchyWhenAddingAxiomThatCreatesCycleThenGetTrueResponseForCycleCheck() {
        OntologyChangeList.Builder<Boolean> changesListBuilder = new OntologyChangeList.Builder<Boolean>();
        changesListBuilder.add(AddAxiomChange.of(checkNotNull(ontologyId), checkNotNull(clsBSubClassOfClsA)));

        var hasCycle = classHierarchyCycleDetector.hasCycle(changesListBuilder.build(true).getChanges());
        assertTrue(hasCycle);
    }

    @Test
    public void getClassesWithCycle() {
    }
}