package edu.stanford.protege.webprotege.events;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.entity.EntityNodeRenderer;
import edu.stanford.protege.webprotege.hierarchy.*;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLDataProperty;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Collections;

import static edu.stanford.protege.webprotege.hierarchy.HierarchyId.DATA_PROPERTY_HIERARCHY;

/**
* Matthew Horridge
* Stanford Center for Biomedical Informatics Research
* 22/05/15
*/
public class OWLDataPropertyHierarchyChangeComputer extends HierarchyChangeComputer<OWLDataProperty> {

    private final DataPropertyHierarchyProvider hierarchyProvider;

    private final EntityNodeRenderer renderer;

    @Inject
    public OWLDataPropertyHierarchyChangeComputer(ProjectId projectId, DataPropertyHierarchyProvider hierarchyProvider, EntityNodeRenderer renderer) {
        super(projectId, EntityType.DATA_PROPERTY, hierarchyProvider, DATA_PROPERTY_HIERARCHY, renderer);
        this.hierarchyProvider = hierarchyProvider;
        this.renderer = renderer;
    }

    @Override
    protected Collection<HighLevelProjectEventProxy> createRemovedEvents(OWLDataProperty child, OWLDataProperty parent) {
        RemoveEdge removeEdge = new RemoveEdge(new GraphEdge(
                new GraphNode(renderer.render(parent)),
                new GraphNode(renderer.render(child))
        ));
        return Collections.singletonList(
                SimpleHighLevelProjectEventProxy.wrap(new EntityHierarchyChangedEvent(getProjectId(), DATA_PROPERTY_HIERARCHY, GraphModelChangedEvent.create(ImmutableList.of(removeEdge))))
        );
    }

    @Override
    protected Collection<HighLevelProjectEventProxy> createAddedEvents(OWLDataProperty child, OWLDataProperty parent) {
        AddEdge addEdge = new AddEdge(new GraphEdge(
                new GraphNode(renderer.render(parent), hierarchyProvider.isLeaf(parent)),
                new GraphNode(renderer.render(child), hierarchyProvider.isLeaf(child))
        ));
        return Collections.singletonList(
                SimpleHighLevelProjectEventProxy.wrap(new EntityHierarchyChangedEvent(getProjectId(), DATA_PROPERTY_HIERARCHY, GraphModelChangedEvent.create(
                        ImmutableList.of(addEdge))))
        );
    }
}
