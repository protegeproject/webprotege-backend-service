package edu.stanford.protege.webprotege.index;

import edu.stanford.protege.webprotege.project.IcdReleasedEntityStatusManager;
import org.jetbrains.annotations.NotNull;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

public class ReleasedClassesManagerImpl implements ReleasedClassesManager {

    @Nonnull
    private final Set<String> releasedClassesIris;

    @Inject
    public ReleasedClassesManagerImpl(@Nonnull IcdReleasedEntityStatusManager icdReleasedClassesManager) {
        checkNotNull(icdReleasedClassesManager);
        this.releasedClassesIris = new HashSet<>();
        releasedClassesIris.addAll(icdReleasedClassesManager.fetchIris());
    }

    @Override
    public boolean isReleased(@NotNull OWLEntity entity) {
        if (entity.isOWLClass()) {
            return releasedClassesIris.contains(entity.asOWLClass().getIRI().toString());
        }
        return false;
    }
}
