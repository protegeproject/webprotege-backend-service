package edu.stanford.protege.webprotege.hierarchy;

import edu.stanford.protege.webprotege.common.ProjectId;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class NamedHierarchyManagerImpl implements NamedHierarchyManager {

    private final Logger logger = LoggerFactory.getLogger(NamedHierarchyManagerImpl.class);

    private final NamedHierarchyRepository hierarchyRepository;

    public NamedHierarchyManagerImpl(OWLDataFactory dataFactory, NamedHierarchyRepository hierarchyRepository) {
        this.hierarchyRepository = hierarchyRepository;
    }

    @Override
    public void saveHierarchy(ProjectId projectId, NamedHierarchy namedHierarchy) {
        logger.info("Saving hierarchy for project: {} {}", projectId, namedHierarchy);
        hierarchyRepository.save(projectId, namedHierarchy);
    }

    @Override
    public List<NamedHierarchy> getHierarchies(ProjectId projectId) {
        var records = hierarchyRepository.find(projectId);
        var result = new ArrayList<NamedHierarchy>();
        result.addAll(BuiltInHierarchyDescriptors.getBuiltInDescriptors());
        result.addAll(records);
        return result;
    }
}
