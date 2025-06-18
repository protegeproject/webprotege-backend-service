package edu.stanford.protege.webprotege.hierarchy;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.DataFactory;
import edu.stanford.protege.webprotege.MockingUtils;
import edu.stanford.protege.webprotege.change.AddAxiomChange;
import edu.stanford.protege.webprotege.change.ChangeApplicationResult;
import edu.stanford.protege.webprotege.change.OntologyChange;
import edu.stanford.protege.webprotege.common.ChangeRequestId;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.entity.EntityNodeRenderer;
import edu.stanford.protege.webprotege.events.EntityHierarchyChangedEventProxyFactory;
import edu.stanford.protege.webprotege.events.EventTranslatorSessionId;
import edu.stanford.protege.webprotege.events.HighLevelProjectEventProxy;
import edu.stanford.protege.webprotege.index.Index;
import edu.stanford.protege.webprotege.index.impl.*;
import edu.stanford.protege.webprotege.revision.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.semanticweb.owlapi.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HierarchyChangesComputerTest {

    private HierarchyChangesComputer computer;

    private ProjectId projectId;

    private ProjectSignatureByTypeIndexImpl projectSignatureByTypeIndex;

    private OWLClass cls;

    private HierarchyProvider<OWLClass> classHierarchyProvider;

    private SubClassOfAxiomsBySubClassIndexImpl subClassOfAxiomsIndex;

    private EquivalentClassesAxiomsIndexImpl equivalentClassesAxiomsIndex;

    private AxiomsByEntityReferenceIndexImpl axiomsByEntityReferenceIndex;

    private OntologyAnnotationsIndexImpl ontologyAnnotationsSignatureIndex;

    private EntitiesInOntologySignatureByIriIndexImpl entitiesInOntologySignatureByIriIndex;

    private EntitiesInProjectSignatureByIriIndexImpl entitiesInProjectSignatureByIriIndex;

    private ClassHierarchyChildrenAxiomsIndexImpl classHierarchyChildrenAxiomsIndex;

    private ProjectOntologiesIndexImpl projectOntologiesIndex;

    @BeforeEach
    public void setup() {
        projectId = ProjectId.generate();
        projectOntologiesIndex = new ProjectOntologiesIndexImpl();
        RevisionManager revisionManager = mock(RevisionManager.class);
        when(revisionManager.getRevisions()).thenReturn(ImmutableList.of());
        projectOntologiesIndex.init(revisionManager);
        subClassOfAxiomsIndex = new SubClassOfAxiomsBySubClassIndexImpl();
        equivalentClassesAxiomsIndex = new EquivalentClassesAxiomsIndexImpl();
        axiomsByEntityReferenceIndex = new AxiomsByEntityReferenceIndexImpl(DataFactory.get());
        ontologyAnnotationsSignatureIndex = new OntologyAnnotationsIndexImpl();
        entitiesInOntologySignatureByIriIndex = new EntitiesInOntologySignatureByIriIndexImpl(
                axiomsByEntityReferenceIndex,
                ontologyAnnotationsSignatureIndex
        );
        entitiesInProjectSignatureByIriIndex = new EntitiesInProjectSignatureByIriIndexImpl(
                projectOntologiesIndex,
                entitiesInOntologySignatureByIriIndex
        );
        projectSignatureByTypeIndex = new ProjectSignatureByTypeIndexImpl(
                axiomsByEntityReferenceIndex
        );
        classHierarchyChildrenAxiomsIndex = new ClassHierarchyChildrenAxiomsIndexImpl(
                projectOntologiesIndex
        );

        cls = MockingUtils.mockOWLClass();


        classHierarchyProvider = new ClassHierarchyProviderImpl(
                projectId,
                Collections.singleton(DataFactory.getOWLThing()),
                projectOntologiesIndex,
                subClassOfAxiomsIndex,
                equivalentClassesAxiomsIndex,
                projectSignatureByTypeIndex,
                entitiesInProjectSignatureByIriIndex,
                classHierarchyChildrenAxiomsIndex
        );
        var eventProxyFactory = new EntityHierarchyChangedEventProxyFactory(
                mock(GraphNodeRenderer.class),
                mock(EntityNodeRenderer.class),
                projectId
        );
        computer = new HierarchyChangesComputer(
                projectId,
                new HierarchyProviderGuard<>(OWLClass.class, classHierarchyProvider),
                ClassHierarchyDescriptor.create(),
                mock(EntityNodeRenderer.class),
                eventProxyFactory
        );
    }


    private void applyOntologyChangesToIndex(List<OntologyChange> ontologyChanges) {
        var indexes = new ArrayList<Index>();
        indexes.add(projectOntologiesIndex);
        indexes.add(projectSignatureByTypeIndex);
        indexes.add(subClassOfAxiomsIndex);
        indexes.add(equivalentClassesAxiomsIndex);
        indexes.add(axiomsByEntityReferenceIndex);
        indexes.add(ontologyAnnotationsSignatureIndex);
        indexes.add(entitiesInOntologySignatureByIriIndex);
        indexes.add(entitiesInProjectSignatureByIriIndex);
        indexes.add(projectSignatureByTypeIndex);
        indexes.add(classHierarchyChildrenAxiomsIndex);

        indexes.stream()
                .filter(index -> index instanceof UpdatableIndex)
                .map(index -> (UpdatableIndex) index)
                .forEach(index -> {
                    index.applyChanges(ImmutableList.copyOf(ontologyChanges));
                });

        ((ClassHierarchyProvider) classHierarchyProvider).handleChanges(ontologyChanges);
    }

    @Test
    void shouldFireAddEdge() {
        var ontologyChanges = List.<OntologyChange>of(
                AddAxiomChange.of(new OWLOntologyID(IRI.create("http://example.org/ont")), DataFactory.get().getOWLDeclarationAxiom(cls))
        );
        var sessionId = EventTranslatorSessionId.create();
        computer.prepareForOntologyChanges(sessionId, ontologyChanges);

        applyOntologyChangesToIndex(ontologyChanges);

        var projectEventList = new ArrayList<HighLevelProjectEventProxy>();
        var changeResult =mock(ChangeApplicationResult.class);
        when(changeResult.getChangeList()).thenReturn(ontologyChanges);

        computer.translateOntologyChanges(sessionId, mock(Revision.class), changeResult, projectEventList, ChangeRequestId.generate());

        assertEquals(1, projectEventList.size());
        var event = projectEventList.get(0).asProjectEvent();
        assertInstanceOf(EntityHierarchyChangedEvent.class, event);
        var hierarchyChangedEvent = (EntityHierarchyChangedEvent) event;
        var graphModelEvent = hierarchyChangedEvent.changeEvent();
        var graphChanges = graphModelEvent.getChanges();
        assertEquals(graphChanges.size(), 1);
        var graphChange = graphChanges.get(0);
        assertInstanceOf(AddEdge.class, graphChange);
        var addEdgeEvent = (AddEdge) graphChange;
    }

    @Test
    void shouldContainSubClass() {
        var ontologyChanges = List.<OntologyChange>of(
                AddAxiomChange.of(new OWLOntologyID(IRI.create("http://example.org/ont")), DataFactory.get().getOWLDeclarationAxiom(cls))
        );
        applyOntologyChangesToIndex(ontologyChanges);
        var children = classHierarchyProvider.getChildren(DataFactory.getOWLThing());
        assertTrue(children.contains(cls));
    }

    @Test
    void shouldHaveOwlThingAsParent() {
        var ontologyChanges = List.<OntologyChange>of(
                AddAxiomChange.of(new OWLOntologyID(IRI.create("http://example.org/ont")), DataFactory.get().getOWLDeclarationAxiom(cls))
        );
        applyOntologyChangesToIndex(ontologyChanges);
        var parents = classHierarchyProvider.getParents(cls);
        assertTrue(parents.contains(DataFactory.getOWLThing()));
    }
}