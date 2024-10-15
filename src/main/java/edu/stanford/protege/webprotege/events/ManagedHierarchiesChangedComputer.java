package edu.stanford.protege.webprotege.events;

import edu.stanford.protege.webprotege.change.ChangeApplicationResult;
import edu.stanford.protege.webprotege.change.OntologyChange;
import edu.stanford.protege.webprotege.common.ChangeRequestId;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.hierarchy.NamedHierarchy;
import edu.stanford.protege.webprotege.hierarchy.NamedHierarchyManager;
import edu.stanford.protege.webprotege.hierarchy.HierarchyProviderManager;
import edu.stanford.protege.webprotege.revision.Revision;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class ManagedHierarchiesChangedComputer implements EventTranslator {

    private final Logger logger = LoggerFactory.getLogger(ManagedHierarchiesChangedComputer.class);

    private final ProjectId projectId;

    private final NamedHierarchyManager hierarchyManager;

    private final HierarchyProviderManager hierarchyProviderManager;

    public ManagedHierarchiesChangedComputer(ProjectId projectId, NamedHierarchyManager namedHierarchyManager, HierarchyProviderManager hierarchyProviderManager) {
        this.projectId = projectId;
        this.hierarchyManager = namedHierarchyManager;
        this.hierarchyProviderManager = hierarchyProviderManager;
    }

    @Override
    public void prepareForOntologyChanges(List<OntologyChange> submittedChanges) {
        hierarchyManager.getHierarchies(projectId)
                .stream()
                .map(NamedHierarchy::hierarchyDescriptor)
                .map(hierarchyProviderManager::getHierarchyChangesComputer)
                .forEach(changeComputer -> changeComputer.ifPresent(cc -> cc.prepareForOntologyChanges(submittedChanges)));
    }

    @Override
    public void translateOntologyChanges(Revision revision, ChangeApplicationResult<?> changes, List<HighLevelProjectEventProxy> projectEventList, ChangeRequestId changeRequestId) {
        logger.info("Translating changes");
        hierarchyManager.getHierarchies(projectId)
                .stream()
                .map(NamedHierarchy::hierarchyDescriptor)
                .map(hierarchyProviderManager::getHierarchyChangesComputer)
                .flatMap(Optional::stream)
                .forEach(changeComputer -> changeComputer.translateOntologyChanges(revision, changes, projectEventList, changeRequestId));
    }
}
