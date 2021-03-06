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
import edu.stanford.protege.webprotege.events.EventTranslator;
import edu.stanford.protege.webprotege.events.HighLevelProjectEventProxy;
import edu.stanford.protege.webprotege.events.SimpleHighLevelProjectEventProxy;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.revision.Revision;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Author: Matthew Horridge<br> Stanford University<br> Bio-Medical Informatics Research Group<br> Date: 21/03/2013
 */
public abstract class HierarchyChangeComputer<T extends OWLEntity> implements EventTranslator {

    private final ProjectId projectId;

    private final HierarchyProvider<T> hierarchyProvider;

    private final EntityType<T> entityType;

    private final HierarchyId hierarchyId;

    private final EntityNodeRenderer renderer;


    private final SetMultimap<T, T> child2ParentMap = HashMultimap.create();

    private final Set<T> roots = new HashSet<>();

    public HierarchyChangeComputer(ProjectId projectId, EntityType<T> entityType, HierarchyProvider<T> hierarchyProvider, HierarchyId hierarchyId, EntityNodeRenderer renderer) {
        this.projectId = projectId;
        this.hierarchyProvider = hierarchyProvider;
        this.entityType = entityType;
        this.hierarchyId = hierarchyId;
        this.renderer = renderer;
    }

    public ProjectId getProjectId() {
        return projectId;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void prepareForOntologyChanges(List<OntologyChange> submittedChanges) {
        for (OntologyChange change : submittedChanges) {
            for (OWLEntity entity : change.getSignature()) {
                if (entity.isType(entityType)) {
                    final T t = (T) entity;
                    final Collection<T> parentsBefore = hierarchyProvider.getParents(t);
                    child2ParentMap.putAll(t, parentsBefore);
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
        Set<T> changeSignature = new HashSet<>();
        for (OntologyChange change : result.getChangeList()) {
            for (OWLEntity child : change.getSignature()) {
                if (child.isType(entityType)) {
                    final T t = (T) child;
                    if (!changeSignature.contains(t)) {
                        changeSignature.add(t);
                        Set<T> parentsBefore = child2ParentMap.get(t);
                        Collection<T> parentsAfter = hierarchyProvider.getParents(t);
                        for (T parentBefore : parentsBefore) {
                            if (!parentsAfter.contains(parentBefore)) {
                                // Removed
                                projectEventList.addAll(createRemovedEvents(t, parentBefore));

                            }
                        }
                        for (T parentAfter : parentsAfter) {
                            if (!parentsBefore.contains(parentAfter)) {
                                // Added
                                projectEventList.addAll(createAddedEvents(t, parentAfter));
                            }
                        }
                    }
                }
            }
        }
        Set<T> rootsAfter = new HashSet<>(hierarchyProvider.getRoots());
        for (T rootAfter : rootsAfter) {
            if (!roots.contains(rootAfter)) {
                ImmutableList<GraphModelChange<EntityNode>> changes = ImmutableList.of(new AddRootNode(
                        new GraphNode(renderer.render(rootAfter),
                                      hierarchyProvider.isLeaf(rootAfter))));
                EntityHierarchyChangedEvent event = new EntityHierarchyChangedEvent(EventId.generate(),
                                                                                    projectId,
                                                                                    hierarchyId,
                                                                                    GraphModelChangedEvent.create(changes));
                projectEventList.add(SimpleHighLevelProjectEventProxy.wrap(event));
            }
        }
        for (T rootBefore : roots) {
            if (!rootsAfter.contains(rootBefore)) {
                ImmutableList<GraphModelChange<EntityNode>> changes = ImmutableList.of(new RemoveRootNode(
                        new GraphNode(renderer.render(rootBefore))));
                EntityHierarchyChangedEvent event = new EntityHierarchyChangedEvent(EventId.generate(),
                                                                                    projectId,
                                                                                    hierarchyId,
                                                                                    GraphModelChangedEvent.create(changes));
                projectEventList.add(SimpleHighLevelProjectEventProxy.wrap(event));
            }
        }
    }


    protected abstract Collection<HighLevelProjectEventProxy> createRemovedEvents(T child, T parent);

    protected abstract Collection<HighLevelProjectEventProxy> createAddedEvents(T child, T parent);

}
