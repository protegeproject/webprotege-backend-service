package edu.stanford.protege.webprotege.entity;

import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.common.*;
import edu.stanford.protege.webprotege.icd.ReleasedClassesChecker;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;


public class EntityStatusManagerImpl implements EntityStatusManager {

    @Nonnull
    private final ReleasedClassesChecker releasedClassesCheckerImpl;

    public EntityStatusManagerImpl(@Nonnull ReleasedClassesChecker releasedClassesCheckerImpl) {
        this.releasedClassesCheckerImpl = checkNotNull(releasedClassesCheckerImpl);
    }

    @Override
    public Set<EntityStatus> getEntityStatuses(OWLEntity entity) {
        var statusSet = new HashSet<EntityStatus>();

        if (releasedClassesCheckerImpl.isReleased(entity)) {
            statusSet.add(EntityStatus.get("released"));
        }

        return ImmutableSet.copyOf(statusSet);
    }
}
