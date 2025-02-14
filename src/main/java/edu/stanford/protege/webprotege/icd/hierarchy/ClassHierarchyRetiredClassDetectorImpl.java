package edu.stanford.protege.webprotege.icd.hierarchy;

import edu.stanford.protege.webprotege.hierarchy.*;
import edu.stanford.protege.webprotege.icd.RetiredClassChecker;
import edu.stanford.protege.webprotege.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.OWLClass;
import org.slf4j.*;

import java.util.Set;
import java.util.stream.Collectors;

@ProjectSingleton
public class ClassHierarchyRetiredClassDetectorImpl implements ClassHierarchyRetiredClassDetector {

    private final ClassHierarchyProvider classHierarchyProvider;

    private final RetiredClassChecker retiredClassesChecker;

    private static final Logger logger = LoggerFactory.getLogger(ClassHierarchyCycleDetectorImpl.class);

    public ClassHierarchyRetiredClassDetectorImpl(ClassHierarchyProvider classHierarchyProvider, RetiredClassChecker retiredClassesChecker) {
        this.classHierarchyProvider = classHierarchyProvider;
        this.retiredClassesChecker = retiredClassesChecker;
    }


    @Override
    public boolean hasRetiredAncestor(OWLClass owlClass) {
        var ancestorsList = classHierarchyProvider.getAncestors(owlClass);
        return ancestorsList.stream().anyMatch(retiredClassesChecker::isRetired);
    }

    @Override
    public Set<OWLClass> getRetiredAncestors(OWLClass owlClass) {
        var ancestorsList = classHierarchyProvider.getAncestors(owlClass);
        return ancestorsList.stream().filter(retiredClassesChecker::isRetired).collect(Collectors.toSet());
    }

    @Override
    public Set<OWLClass> getClassesWithRetiredAncestors(Set<OWLClass> owlClasses) {
        return owlClasses.stream().filter(this::isRetired).collect(Collectors.toSet());
    }

    @Override
    public boolean isRetired(OWLClass owlClass) {
        return retiredClassesChecker.isRetired(owlClass) || hasRetiredAncestor(owlClass);
    }
}
