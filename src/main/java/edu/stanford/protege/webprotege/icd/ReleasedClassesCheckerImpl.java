package edu.stanford.protege.webprotege.icd;

import org.jetbrains.annotations.NotNull;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

public class ReleasedClassesCheckerImpl implements ReleasedClassesChecker {

    @Nonnull
    private final IcdReleasedEntityStatusManager icdReleasedEntityStatusManager;

    @Nonnull
    private final Set<String> releasedClassesIris;

    @Inject
    public ReleasedClassesCheckerImpl(@Nonnull IcdReleasedEntityStatusManager icdReleasedClassesManager) {
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

    private boolean isNotOwlClass(OWLEntity entity) {
        return !entity.isOWLClass();
    }
}
