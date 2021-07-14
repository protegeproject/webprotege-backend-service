package edu.stanford.protege.webprotege.events;

import edu.stanford.protege.webprotege.entity.EntityNodeRenderer;
import edu.stanford.protege.webprotege.hierarchy.ClassHierarchyProvider;
import edu.stanford.protege.webprotege.hierarchy.GraphModelChangedEvent;
import edu.stanford.protege.webprotege.hierarchy.GraphNodeRenderer;
import edu.stanford.protege.webprotege.hierarchy.HierarchyId;
import edu.stanford.protege.webprotege.project.ProjectId;
import org.semanticweb.owlapi.model.OWLClass;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-07-13
 */
public class EntityHierarchyChangedEventProxyFactory {

    private final GraphNodeRenderer graphNodeRenderer;

    private final EntityNodeRenderer entityNodeRenderer;

    private final ProjectId projectId;

    public EntityHierarchyChangedEventProxyFactory(GraphNodeRenderer graphNodeRenderer,
                                                   EntityNodeRenderer entityNodeRenderer,
                                                   ProjectId projectId) {
        this.graphNodeRenderer = graphNodeRenderer;
        this.entityNodeRenderer = entityNodeRenderer;
        this.projectId = projectId;
    }

    public EntityHierarchyChangedEventProxy create(GraphModelChangedEvent<OWLClass> event,
                                                   ClassHierarchyProvider classHierarchyProvider,
                                                   HierarchyId hierarchyId) {
        return new EntityHierarchyChangedEventProxy(event,
                                                    graphNodeRenderer,
                                                    entityNodeRenderer,
                                                    classHierarchyProvider,
                                                    projectId,
                                                    hierarchyId);
    }
}
