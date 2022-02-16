package edu.stanford.protege.webprotege.events;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.common.EventId;
import edu.stanford.protege.webprotege.entity.EntityNodeRenderer;
import edu.stanford.protege.webprotege.hierarchy.*;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;

import javax.inject.Inject;
import java.util.Collection;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.protege.webprotege.hierarchy.HierarchyId.ANNOTATION_PROPERTY_HIERARCHY;
import static java.util.Collections.singletonList;

/**
* Matthew Horridge
* Stanford Center for Biomedical Informatics Research
* 22/05/15
*/
public class OWLAnnotationPropertyHierarchyChangeComputer extends HierarchyChangeComputer<OWLAnnotationProperty> {

    private final EntityNodeRenderer renderer;

    private final AnnotationPropertyHierarchyProvider hierarchyProvider;

    @Inject
    public OWLAnnotationPropertyHierarchyChangeComputer(ProjectId projectId, AnnotationPropertyHierarchyProvider hierarchyProvider, EntityNodeRenderer renderer) {
        super(projectId, EntityType.ANNOTATION_PROPERTY, hierarchyProvider, ANNOTATION_PROPERTY_HIERARCHY, renderer);
        this.renderer = checkNotNull(renderer);
        this.hierarchyProvider = checkNotNull(hierarchyProvider);
    }

    @Override
    protected Collection<HighLevelProjectEventProxy> createRemovedEvents(OWLAnnotationProperty child, OWLAnnotationProperty parent) {
        RemoveEdge removeEdge = new RemoveEdge(new GraphEdge(
                new GraphNode(renderer.render(parent)),
                new GraphNode(renderer.render(child))
        ));
        return singletonList(
               SimpleHighLevelProjectEventProxy.wrap(new EntityHierarchyChangedEvent(EventId.generate(), getProjectId(), ANNOTATION_PROPERTY_HIERARCHY, GraphModelChangedEvent.create(
                       ImmutableList.of(removeEdge))))
        );
    }

    @Override
    protected Collection<HighLevelProjectEventProxy> createAddedEvents(OWLAnnotationProperty child, OWLAnnotationProperty parent) {
        AddEdge addEdge = new AddEdge(new GraphEdge(
                new GraphNode(renderer.render(parent), hierarchyProvider.isLeaf(parent)),
                new GraphNode(renderer.render(child), hierarchyProvider.isLeaf(child))
        ));
        return singletonList(
                SimpleHighLevelProjectEventProxy.wrap(new EntityHierarchyChangedEvent(EventId.generate(), getProjectId(), ANNOTATION_PROPERTY_HIERARCHY, GraphModelChangedEvent.create(ImmutableList.of(addEdge))))
        );
    }
}
