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

    private final EventTranslatorSessionChecker sessionChecker = new EventTranslatorSessionChecker();

    public ManagedHierarchiesChangedComputer(ProjectId projectId, NamedHierarchyManager namedHierarchyManager, HierarchyProviderManager hierarchyProviderManager) {
        this.projectId = projectId;
        this.hierarchyManager = namedHierarchyManager;
        this.hierarchyProviderManager = hierarchyProviderManager;
    }

    @Override
    public void prepareForOntologyChanges(EventTranslatorSessionId sessionId, List<OntologyChange> submittedChanges) {
        sessionChecker.startSession(sessionId);
        hierarchyManager.getNamedHierarchies(projectId)
                .stream()
                .map(NamedHierarchy::hierarchyDescriptor)
                .peek(hierarchyDescriptor -> logger.info("Preparing for ontology changes in {}", hierarchyDescriptor))
                .map(hierarchyProviderManager::getHierarchyChangesComputer)
                .peek(hierarchyChangesComputer -> logger.info("{} Preparing for ontology changes with hierarchy change computer: {}", sessionId, hierarchyChangesComputer))
                .forEach(changeComputer -> changeComputer.ifPresent(cc -> cc.prepareForOntologyChanges(sessionId, submittedChanges)));
    }

    @Override
    public void translateOntologyChanges(EventTranslatorSessionId sessionId, Revision revision, ChangeApplicationResult<?> changes, List<HighLevelProjectEventProxy> projectEventList, ChangeRequestId changeRequestId) {
        sessionChecker.finishSession(sessionId);
        hierarchyManager.getNamedHierarchies(projectId)
                .stream()
                .map(NamedHierarchy::hierarchyDescriptor)
                .map(hierarchyProviderManager::getHierarchyChangesComputer)
                .flatMap(Optional::stream)
                .peek(hierarchyChangesComputer -> logger.info("{} Processing ontology changes with computer: {}", sessionId, hierarchyChangesComputer))
                .forEach(changeComputer -> changeComputer.translateOntologyChanges(sessionId, revision, changes, projectEventList, changeRequestId));
    }
}
