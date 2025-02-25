package edu.stanford.protege.webprotege.hierarchy;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.SetMultimap;
import edu.stanford.protege.webprotege.change.ChangeApplicationResult;
import edu.stanford.protege.webprotege.change.OntologyChange;
import edu.stanford.protege.webprotege.common.ChangeRequestId;
import edu.stanford.protege.webprotege.common.EventId;
import edu.stanford.protege.webprotege.entity.EntityNode;
import edu.stanford.protege.webprotege.entity.EntityNodeRenderer;
import edu.stanford.protege.webprotege.events.EntityHierarchyChangedEventProxyFactory;
import edu.stanford.protege.webprotege.events.EventTranslator;
import edu.stanford.protege.webprotege.events.HighLevelProjectEventProxy;
import edu.stanford.protege.webprotege.events.SimpleHighLevelProjectEventProxy;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.revision.Revision;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Author: Matthew Horridge<br> Stanford University<br> Bio-Medical Informatics Research Group<br> Date: 21/03/2013
 */
public final class HierarchyChangesComputer implements EventTranslator {

    private final ProjectId projectId;

    private final HierarchyProvider<OWLEntity> hierarchyProvider;

    private final HierarchyDescriptor hierarchyDescriptor;

    private final EntityNodeRenderer renderer;


    private final SetMultimap<OWLEntity, OWLEntity> child2ParentMap = HashMultimap.create();

    private final Set<OWLEntity> roots = new HashSet<>();

    private final EntityHierarchyChangedEventProxyFactory proxyFactory;

    public HierarchyChangesComputer(ProjectId projectId, HierarchyProvider<OWLEntity> hierarchyProvider, HierarchyDescriptor hierarchyDescriptor, EntityNodeRenderer renderer, EntityHierarchyChangedEventProxyFactory proxyFactory) {
        this.projectId = projectId;
        this.hierarchyProvider = hierarchyProvider;
        this.hierarchyDescriptor = hierarchyDescriptor;
        this.renderer = renderer;
        this.proxyFactory = proxyFactory;
    }

    public ProjectId getProjectId() {
        return projectId;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void prepareForOntologyChanges(List<OntologyChange> submittedChanges) {
        for (OntologyChange change : submittedChanges) {
            for (OWLEntity entity : change.getSignature()) {
                if (hierarchyProvider.contains(entity)) {
                    final Collection<OWLEntity> parentsBefore = hierarchyProvider.getParents(entity);
                    child2ParentMap.putAll(entity, parentsBefore);
                }
            }
        }
        roots.addAll(hierarchyProvider.getRoots());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void translateOntologyChanges(Revision revision,
                                         ChangeApplicationResult<?> result,
                                         List<HighLevelProjectEventProxy> projectEventList,
                                         ChangeRequestId changeRequestId) {
        Set<OWLEntity> changeSignature = new HashSet<>();
        for (OntologyChange change : result.getChangeList()) {
            for (OWLEntity child : change.getSignature()) {
                if (hierarchyProvider.contains(child)) {
                    if (!changeSignature.contains(child)) {
                        changeSignature.add(child);
                        Set<OWLEntity> parentsBefore = child2ParentMap.get(child);
                        Collection<OWLEntity> parentsAfter = hierarchyProvider.getParents(child);
                        for (OWLEntity parentBefore : parentsBefore) {
                            if (!parentsAfter.contains(parentBefore)) {
                                // Removed
                                projectEventList.addAll(createRemovedEvents(child, parentBefore));

                            }
                        }
                        for (OWLEntity parentAfter : parentsAfter) {
                            if (!parentsBefore.contains(parentAfter)) {
                                // Added
                                projectEventList.addAll(createAddedEvents(child, parentAfter));
                            }
                        }
                    }
                }
            }
        }
        Set<OWLEntity> rootsAfter = new HashSet<>(hierarchyProvider.getRoots());
        for (OWLEntity rootAfter : rootsAfter) {
            if (!roots.contains(rootAfter)) {
                ImmutableList<GraphModelChange<EntityNode>> changes = ImmutableList.of(new AddRootNode<EntityNode>(
                        new GraphNode<>(renderer.render(rootAfter),
                                      hierarchyProvider.isLeaf(rootAfter))));
                EntityHierarchyChangedEvent event = new EntityHierarchyChangedEvent(EventId.generate(),
                                                                                    projectId,
                                                                                    hierarchyDescriptor,
                                                                                    GraphModelChangedEvent.create(changes));
                projectEventList.add(SimpleHighLevelProjectEventProxy.wrap(event));
            }
        }
        for (OWLEntity rootBefore : roots) {
            if (!rootsAfter.contains(rootBefore)) {
                ImmutableList<GraphModelChange<EntityNode>> changes = ImmutableList.of(new RemoveRootNode<>(
                        new GraphNode<>(renderer.render(rootBefore))));
                EntityHierarchyChangedEvent event = new EntityHierarchyChangedEvent(EventId.generate(),
                                                                                    projectId,
                                                                                    hierarchyDescriptor,
                                                                                    GraphModelChangedEvent.create(changes));
                projectEventList.add(SimpleHighLevelProjectEventProxy.wrap(event));
            }
        }
    }

    private Collection<HighLevelProjectEventProxy> createRemovedEvents(OWLEntity child, OWLEntity parent) {
        var removeEdge = new RemoveEdge<>(
                new GraphEdge<>(
                        new GraphNode<>(parent, hierarchyProvider.isLeaf(parent)),
                        new GraphNode<>(child, hierarchyProvider.isLeaf(child))
                )
        );
        var event = GraphModelChangedEvent.create(ImmutableList.of(removeEdge));
        var proxyEvent = proxyFactory.create(event, hierarchyProvider, hierarchyDescriptor);
        return ImmutableList.of(proxyEvent);
    }

    private Collection<HighLevelProjectEventProxy> createAddedEvents(OWLEntity child, OWLEntity parent) {
        var addEdge = new AddEdge<>(
                new GraphEdge<>(
                        new GraphNode<>(parent, hierarchyProvider.isLeaf(parent)),
                        new GraphNode<>(child, hierarchyProvider.isLeaf(child))
                )
        );
        var event = GraphModelChangedEvent.create(ImmutableList.of(addEdge));
        var proxyEvent = proxyFactory.create(event, hierarchyProvider, hierarchyDescriptor);
        return ImmutableList.of(proxyEvent);
    }

}
