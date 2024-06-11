package edu.stanford.protege.webprotege.entity;

import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.index.ReleasedClassesManager;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.Set;

import static org.glassfish.jersey.internal.guava.Preconditions.checkNotNull;

public class EntityStatusManagerImpl implements EntityStatusManager {

    @Nonnull
    private final ReleasedClassesManager releasedClassesManagerImpl;

    public EntityStatusManagerImpl(@Nonnull ReleasedClassesManager releasedClassesManagerImpl) {
        this.releasedClassesManagerImpl = checkNotNull(releasedClassesManagerImpl);
    }

    @Override
    public Set<EntityStatus> getEntityStatuses(ProjectId projectId, OWLEntity entity) {
        if(releasedClassesManagerImpl.isReleased(entity)){
            return Set.of(EntityStatus.get("released"));
        }
        return Set.of();
    }
}
