package edu.stanford.protege.webprotege.events;

import edu.stanford.protege.webprotege.change.ChangeApplicationResult;
import edu.stanford.protege.webprotege.change.OntologyChange;
import edu.stanford.protege.webprotege.common.ChangeRequestId;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.hierarchy.*;
import edu.stanford.protege.webprotege.revision.Revision;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ManagedHierarchiesChangedComputer implements EventTranslator {

    private final Logger logger = LoggerFactory.getLogger(ManagedHierarchiesChangedComputer.class);

    private final ProjectId projectId;

    private final NamedHierarchyManager hierarchyManager;

    private final HierarchyProviderManager hierarchyProviderManager;

    private final EventTranslatorSessionChecker sessionChecker = new EventTranslatorSessionChecker();

    private final HierarchyChangesComputerFactory hierarchyChangesComputerFactory;

    private Map<HierarchyDescriptor, HierarchyChangesComputer> changesComputerMap = new HashMap<>();


    public ManagedHierarchiesChangedComputer(ProjectId projectId, NamedHierarchyManager namedHierarchyManager, HierarchyProviderManager hierarchyProviderManager, HierarchyChangesComputerFactory hierarchyChangesComputerFactory) {
        this.projectId = projectId;
        this.hierarchyManager = namedHierarchyManager;
        this.hierarchyProviderManager = hierarchyProviderManager;
        this.hierarchyChangesComputerFactory = hierarchyChangesComputerFactory;
    }

    @Override
    public void prepareForOntologyChanges(EventTranslatorSessionId sessionId, List<OntologyChange> submittedChanges) {
        sessionChecker.startSession(sessionId);
        hierarchyManager.getNamedHierarchies(projectId)
                .stream()
                .map(NamedHierarchy::hierarchyDescriptor)
                .peek(hierarchyDescriptor -> logger.info("Preparing for ontology changes in {}", hierarchyDescriptor))
                .map(this::setupChangesComputer)
                .peek(hierarchyChangesComputer -> logger.info("{} Preparing for ontology changes with hierarchy change computer: {}", sessionId, hierarchyChangesComputer))
                .forEach(changeComputer -> changeComputer.ifPresent(cc -> cc.prepareForOntologyChanges(sessionId, submittedChanges)));
    }

    private Optional<HierarchyChangesComputer> setupChangesComputer(HierarchyDescriptor descriptor) {
        var hp = hierarchyProviderManager.getHierarchyProvider(descriptor);
        var hcc = hp.map(p -> {
            return hierarchyChangesComputerFactory.getComputer(descriptor, p);
        });
        hcc.ifPresent(comp -> changesComputerMap.put(descriptor, comp));
        return hcc;
    }

    private Optional<HierarchyChangesComputer> getChangesComputer(HierarchyDescriptor descriptor) {
        return Optional.ofNullable(changesComputerMap.get(descriptor));
    }

    @Override
    public void translateOntologyChanges(EventTranslatorSessionId sessionId, Revision revision, ChangeApplicationResult<?> changes, List<HighLevelProjectEventProxy> projectEventList, ChangeRequestId changeRequestId) {
        sessionChecker.finishSession(sessionId);
        hierarchyManager.getNamedHierarchies(projectId)
                .stream()
                .map(NamedHierarchy::hierarchyDescriptor)
                .map(this::getChangesComputer)
                .flatMap(Optional::stream)
                .peek(hierarchyChangesComputer -> logger.info("{} Processing ontology changes with computer: {}", sessionId, hierarchyChangesComputer))
                .forEach(changeComputer -> changeComputer.translateOntologyChanges(sessionId, revision, changes, projectEventList, changeRequestId));
    }

    @Override
    public void closeSession(EventTranslatorSessionId sessionId) {
        sessionChecker.finishSession(sessionId);
        hierarchyManager.getNamedHierarchies(projectId)
                .stream()
                .map(NamedHierarchy::hierarchyDescriptor)
                .peek(hierarchyDescriptor -> logger.info("Closing session for {}", hierarchyDescriptor))
                .map(hierarchyProviderManager::getHierarchyChangesComputer)
                .peek(hierarchyChangesComputer -> logger.info("{} Closing session for : {}", sessionId, hierarchyChangesComputer))
                .forEach(changeComputer -> changeComputer.ifPresent(cc -> cc.closeSession(sessionId)));
    }
}
