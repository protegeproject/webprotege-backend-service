package edu.stanford.protege.webprotege.hierarchy;

import edu.stanford.protege.webprotege.change.OntologyChange;
import edu.stanford.protege.webprotege.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.*;
import org.slf4j.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.protege.webprotege.hierarchy.ClassHierarchyProvider.filterIrrelevantChanges;

@ProjectSingleton
public class ClassHierarchyCycleDetectorImpl implements ClassHierarchyCycleDetector {

    private final ClassHierarchyProvider classHierarchyProvider;

    private static final Logger logger = LoggerFactory.getLogger(ClassHierarchyCycleDetectorImpl.class);


    @Inject
    public ClassHierarchyCycleDetectorImpl(@Nonnull ClassHierarchyProvider classHierarchyProvider) {
        this.classHierarchyProvider = checkNotNull(classHierarchyProvider);
    }

    @Override
    public boolean hasCycle(List<OntologyChange> changes) {
        var filteredChangedOwlClasses = filterIrrelevantChanges(changes)
                .stream()
//                .filter(OntologyChange::isRemoveAxiom)
//                .filter(OntologyChange::isAddAxiom)
                .flatMap(change -> change.getSignature()
                        .stream()
                        .filter(OWLEntity::isOWLClass)
                        .filter(entity -> !classHierarchyProvider.getRoots().contains(entity))
                        .map(entity -> (OWLClass) entity))
                .toList();
        for (OWLClass changedClass : filteredChangedOwlClasses) {
            if (classHierarchyProvider.isAncestor(changedClass, changedClass)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Set<OWLClass> getClassesWithCycle(List<OntologyChange> changes) {
        var result = new HashSet<OWLClass>();
        var filteredChangedOwlClasses = filterIrrelevantChanges(changes)
                .stream()
                .filter(OntologyChange::isAxiomChange)
                .flatMap(change -> change.getSignature()
                        .stream()
                        .filter(OWLEntity::isOWLClass)
                        .filter(entity -> !classHierarchyProvider.getRoots().contains(entity))
                        .map(entity -> (OWLClass) entity))
                .toList();
        for (OWLClass changedClass : filteredChangedOwlClasses) {
            if (classHierarchyProvider.isAncestor(changedClass, changedClass)) {
                result.add(changedClass);
            }
        }
        return result;
    }
}
