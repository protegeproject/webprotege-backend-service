package edu.stanford.protege.webprotege.events;

import edu.stanford.protege.webprotege.entity.EntityNodeRenderer;
import edu.stanford.protege.webprotege.hierarchy.*;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;

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

    public EntityHierarchyChangedEventProxy create(GraphModelChangedEvent<OWLEntity> event,
                                                   HierarchyProvider<OWLEntity> classHierarchyProvider,
                                                   HierarchyDescriptor hierarchyDescriptor) {
        return new EntityHierarchyChangedEventProxy(event,
                                                    graphNodeRenderer,
                                                    entityNodeRenderer,
                                                    classHierarchyProvider,
                                                    projectId,
                                                    hierarchyDescriptor);
    }
}
