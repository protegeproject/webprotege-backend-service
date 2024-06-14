package edu.stanford.protege.webprotege.icd.hierarchy;

import edu.stanford.protege.webprotege.hierarchy.*;
import edu.stanford.protege.webprotege.icd.RetiredClassChecker;
import edu.stanford.protege.webprotege.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.OWLClass;
import org.slf4j.*;

import java.util.Set;
import java.util.stream.Collectors;

@ProjectSingleton
public class ClassHierarchyRetiredAncestorDetectorImpl implements ClassHierarchyRetiredAncestorDetector {

    private final ClassHierarchyProvider classHierarchyProvider;

    private final RetiredClassChecker retiredClassesIndex;

    private static final Logger logger = LoggerFactory.getLogger(ClassHierarchyCycleDetectorImpl.class);

    public ClassHierarchyRetiredAncestorDetectorImpl(ClassHierarchyProvider classHierarchyProvider, RetiredClassChecker retiredClassesIndex) {
        this.classHierarchyProvider = classHierarchyProvider;
        this.retiredClassesIndex = retiredClassesIndex;
    }


    @Override
    public boolean hasRetiredAncestor(OWLClass owlClass) {
        var ancestorsList = classHierarchyProvider.getAncestors(owlClass);
        return ancestorsList.stream().anyMatch(retiredClassesIndex::isRetired);
    }

    @Override
    public Set<OWLClass> getRetiredAncestors(OWLClass owlClass) {
        var ancestorsList = classHierarchyProvider.getAncestors(owlClass);
        return ancestorsList.stream().filter(retiredClassesIndex::isRetired).collect(Collectors.toSet());
    }

    @Override
    public Set<OWLClass> getClassesWithRetiredAncestors(Set<OWLClass> owlClasses) {
        return owlClasses.stream().filter(this::hasRetiredAncestor).collect(Collectors.toSet());
    }
}
