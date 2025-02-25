package edu.stanford.protege.webprotege.watches;

import edu.stanford.protege.webprotege.common.EventId;
import edu.stanford.protege.webprotege.inject.ProjectSingleton;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.ipc.EventDispatcher;
import org.semanticweb.owlapi.model.OWLEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.protege.webprotege.watches.WatchType.BRANCH;
import static java.util.Collections.singleton;
import static java.util.stream.Collectors.toSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Apr 2017
 */
@ProjectSingleton
public class WatchManagerImpl implements WatchManager {

    private static final Logger logger = LoggerFactory.getLogger(WatchManagerImpl.class);

    private final ProjectId projectId;

    private final WatchRecordRepository repository;

    private final IndirectlyWatchedEntitiesFinder indirectlyWatchedEntitiesFinder;

    private final WatchTriggeredHandler watchTriggeredHandler;

    private final EventDispatcher eventDispatcher;

    private boolean attached = false;

    @Inject
    public WatchManagerImpl(@Nonnull ProjectId projectId,
                            @Nonnull WatchRecordRepository repository,
                            @Nonnull IndirectlyWatchedEntitiesFinder indirectlyWatchedEntitiesFinder,
                            @Nonnull WatchTriggeredHandler watchTriggeredHandler,
                            @Nonnull EventDispatcher eventDispatcher) {
        this.projectId = checkNotNull(projectId);
        this.repository = checkNotNull(repository);
        this.indirectlyWatchedEntitiesFinder = checkNotNull(indirectlyWatchedEntitiesFinder);
        this.watchTriggeredHandler = checkNotNull(watchTriggeredHandler);
        this.eventDispatcher = checkNotNull(eventDispatcher);
    }

    public synchronized void attach() {
        if(attached) {
            return;
        }
        attached = true;
        // Note, there is no need to keep hold of Handler Registrations here as these will be cleaned up and
        // terminated when the relevant project is disposed.
     }

    @Override
    public Set<Watch> getWatches(@Nonnull UserId userId) {
        return repository.findWatchRecords(projectId, userId).stream()
                         .map(this::toWatch)
                         .collect(toSet());
    }

    @Override
    public void addWatch(@Nonnull Watch watch) {
        repository.saveWatchRecord(toWatchRecord(watch));
        eventDispatcher.dispatchEvent(new WatchAddedEvent(EventId.generate(), projectId, watch));
    }

    @Override
    public void removeWatch(@Nonnull Watch watch) {
        repository.deleteWatchRecord(toWatchRecord(watch));
        eventDispatcher.dispatchEvent(new WatchRemovedEvent(EventId.generate(), projectId, watch));
    }

    @Override
    public Set<Watch> getDirectWatches(@Nonnull OWLEntity watchedEntity) {
        return repository.findWatchRecords(projectId, singleton(watchedEntity)).stream()
                .map(this::toWatch)
                .collect(toSet());
    }

    @Override
    public Set<Watch> getDirectWatches(@Nonnull OWLEntity watchedObject, @Nonnull UserId userId) {
        return repository.findWatchRecords(projectId,
                                           userId,
                                           singleton(watchedObject)).stream()
                         .map(this::toWatch)
                         .collect(toSet());
    }

    private void handleEntityFrameChanged(@Nonnull OWLEntity entity, @Nonnull UserId byUser) {
        Collection<Watch> watches = findWatchRecordsForEntity(entity);
        if (watches.isEmpty()) {
            return;
        }
        Set<UserId> userIds = watches.stream()
                                     .map(Watch::getUserId)
                                     .collect(toSet());
        watchTriggeredHandler.handleWatchTriggered(userIds, entity, byUser);

    }

    private Collection<Watch> findWatchRecordsForEntity(OWLEntity entity) {
        List<OWLEntity> entities = new ArrayList<>(indirectlyWatchedEntitiesFinder.getRelatedWatchedEntities(entity));
        entities.add(entity);
        return repository.findWatchRecords(projectId, entities).stream()
                         .distinct()
                         // Filter so that we have watch records that are either watching a branch containing
                         // this entity or records that are directly watching this entity
                         .filter(r -> r.getType() == BRANCH || r.getEntity().equals(entity))
                         .map(this::toWatch)
                         .collect(toSet());
    }


    private WatchRecord toWatchRecord(Watch watch) {
        return new WatchRecord(projectId, watch.getUserId(), watch.getEntity(), watch.getType());
    }

    private Watch toWatch(WatchRecord record) {
        return Watch.create(record.getUserId(), record.getEntity(), record.getType());
    }
}
