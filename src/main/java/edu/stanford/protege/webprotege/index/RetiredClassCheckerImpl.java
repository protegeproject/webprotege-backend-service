package edu.stanford.protege.webprotege.index;

import edu.stanford.protege.webprotege.project.*;
import org.jetbrains.annotations.NotNull;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

public class RetiredClassCheckerImpl implements RetiredClassChecker {

    @Nonnull
    private final Set<String> retiredClassesIndex;

    @Inject
    public RetiredClassCheckerImpl(@Nonnull IcdRetiredClassManager icdRetiredClassesManager) {
        checkNotNull(icdRetiredClassesManager);
        this.retiredClassesIndex = new HashSet<>();
        retiredClassesIndex.addAll(icdRetiredClassesManager.fetchIris());
    }

    @Override
    public boolean isRetired(@NotNull OWLEntity entity) {
        if (entity.isOWLClass()) {
            return retiredClassesIndex.contains(entity.asOWLClass().getIRI().toString());
        }
        return false;
    }
}
