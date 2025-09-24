package edu.stanford.protege.webprotege.icd;

import edu.stanford.protege.webprotege.hierarchy.ClassHierarchyProvider;
import org.jetbrains.annotations.NotNull;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

public class ReleasedClassesCheckerImpl implements ReleasedClassesChecker {

    @Nonnull
    private final IcdReleasedEntityStatusManager icdReleasedEntityStatusManager;

    @Nonnull
    private final ClassHierarchyProvider classHierarchyProvider;

    @Nonnull
    private final Set<String> releasedClassesIris;

    @Inject
    public ReleasedClassesCheckerImpl(@Nonnull IcdReleasedEntityStatusManager icdReleasedClassesManager,
                                      @Nonnull ClassHierarchyProvider classHierarchyProvider) {
        this.classHierarchyProvider = classHierarchyProvider;
        this.releasedClassesIris = new HashSet<>();
        icdReleasedEntityStatusManager = checkNotNull(icdReleasedClassesManager);
    }

    @Override
    public boolean isReleased(@NotNull OWLEntity entity) {
        if (isNotOwlClass(entity)) {
            return false;
        }

        if (releasedClassesIris.isEmpty()) {
            releasedClassesIris.addAll(icdReleasedEntityStatusManager.fetchIris());
        }

        return releasedClassesIris.contains(entity.asOWLClass().getIRI().toString());
    }

    @Override
    public Set<OWLClass> getReleasedDescendants(OWLEntity entity) {
        Set<OWLClass> response = new HashSet<>();
        if (isNotOwlClass(entity)) {
            return new HashSet<>();
        }

        if (releasedClassesIris.isEmpty()) {
            releasedClassesIris.addAll(icdReleasedEntityStatusManager.fetchIris());
        }

        if(releasedClassesIris.contains(entity.getIRI().toString())) {
            response.add(entity.asOWLClass());
        }

         response.addAll(classHierarchyProvider.getDescendants(entity.asOWLClass()).stream()
                 .filter(descendant -> releasedClassesIris.contains(descendant.getIRI().toString()))
                 .collect(Collectors.toSet()));
        return response;
    }


    private boolean isNotOwlClass(OWLEntity entity) {
        return !entity.isOWLClass();
    }
}
