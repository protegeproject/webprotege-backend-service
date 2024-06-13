package edu.stanford.protege.webprotege.hierarchy;

import edu.stanford.protege.webprotege.change.OntologyChange;
import edu.stanford.protege.webprotege.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

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
        var filteredChangedOwlClasses = classHierarchyProvider.filterIrrelevantChanges(changes)
                .stream()
                .filter(OntologyChange::isAxiomChange)
                .flatMap(change -> change.getSignature()
                        .stream()
                        .filter(OWLEntity::isOWLClass)
                        .map(OWLEntity::asOWLClass)
                        .filter(owlClass -> !classHierarchyProvider.getRoots().contains(owlClass))
                )
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
        var filteredChangedOwlClasses = classHierarchyProvider.filterIrrelevantChanges(changes)
                .stream()
                .filter(OntologyChange::isAxiomChange)
                .flatMap(change -> change.getSignature()
                        .stream()
                        .filter(OWLEntity::isOWLClass)
                        .map(OWLEntity::asOWLClass)
                        .filter(owlClass -> !classHierarchyProvider.getRoots().contains(owlClass))
                )
                .collect(Collectors.toSet());
        for (OWLClass changedClass : filteredChangedOwlClasses) {
            if (classHierarchyProvider.isAncestor(changedClass, changedClass)) {
                result.add(changedClass);
            }
        }
        return result;
    }
}
