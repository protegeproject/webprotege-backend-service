package edu.stanford.protege.webprotege.events;

import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.entity.EntityNodeRenderer;
import edu.stanford.protege.webprotege.hierarchy.HierarchyChangesComputer;
import edu.stanford.protege.webprotege.hierarchy.HierarchyDescriptor;
import edu.stanford.protege.webprotege.hierarchy.HierarchyId;
import edu.stanford.protege.webprotege.hierarchy.HierarchyProvider;
import org.semanticweb.owlapi.model.OWLEntity;

public class HierarchyChangesComputerFactory {


    private final ProjectId projectId;

    private final EntityNodeRenderer entityNodeRenderer;

    private final EntityHierarchyChangedEventProxyFactory proxyFactory;

    public HierarchyChangesComputerFactory(ProjectId projectId, EntityNodeRenderer entityNodeRenderer, EntityHierarchyChangedEventProxyFactory proxyFactory) {
        this.projectId = projectId;
        this.entityNodeRenderer = entityNodeRenderer;
        this.proxyFactory = proxyFactory;
    }

    public HierarchyChangesComputer getComputer(HierarchyDescriptor hierarchyDescriptor,
                                                HierarchyProvider<OWLEntity> hierarchyProvider) {
        return new HierarchyChangesComputer(projectId,
                hierarchyProvider,
                hierarchyDescriptor,
                entityNodeRenderer,
                proxyFactory
        );
    }
}
