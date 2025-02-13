package edu.stanford.protege.webprotege.watches;

import edu.stanford.protege.webprotege.common.EventId;
import edu.stanford.protege.webprotege.inject.ProjectSingleton;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.ipc.EventDispatcher;
import org.semanticweb.owlapi.model.IRI;
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
import static java.util.stream.Collectors.toSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Apr 2017
 */
@ProjectSingleton
public class WatchManagerImpl implements WatchManager {

    private final ProjectId projectId;

    private final WatchRecordRepository repository;

    private final IndirectlyWatchedEntitiesFinder indirectlyWatchedEntitiesFinder;

    private final WatchTriggeredHandler watchTriggeredHandler;

    private final EventDispatcher eventDispatcher;

    private final Map<IRI, List<Watch>> cache = new ConcurrentHashMap<>();

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
        populateCache(projectId);
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
    public Stream<Watch> getWatches(@Nonnull UserId userId) {
        return cache.values().stream()
                .flatMap(Collection::stream)
                .filter(watch -> watch.getUserId().equals(userId));

    }

    @Override
    public void addWatch(@Nonnull Watch watch) {
        repository.saveWatchRecord(toWatchRecord(watch));

        List<Watch> watches = cache.get(watch.getEntity().getIRI());
        if(watches == null) {
            watches = new ArrayList<>();
        }

        watches.add(watch);
        cache.put(watch.getEntity().getIRI(), watches);

        eventDispatcher.dispatchEvent(new WatchAddedEvent(EventId.generate(), projectId, watch));
    }

    @Override
    public void removeWatch(@Nonnull Watch watch) {
        repository.deleteWatchRecord(toWatchRecord(watch));
        var existing = cache.get(watch.getEntity().getIRI());
        if(existing != null) {
            existing.remove(watch);
            cache.put(watch.getEntity().getIRI(), existing);
        }
        eventDispatcher.dispatchEvent(new WatchRemovedEvent(EventId.generate(), projectId, watch));
    }

    @Override
    public Set<Watch> getDirectWatches(@Nonnull OWLEntity watchedEntity) {

        var response = cache.get(watchedEntity.getIRI());
        if(response != null) {
            return new HashSet<>(response);
        }

        return new HashSet<>();
    }

    @Override
    public Set<Watch> getDirectWatches(@Nonnull OWLEntity watchedObject, @Nonnull UserId userId) {

        var response = cache.get(watchedObject.getIRI());

        if(response != null) {
            return response.stream().filter(watch -> watch.getUserId().equals(userId)).collect(toSet());
        }

        return new HashSet<>();
    }

    private synchronized void populateCache(ProjectId projectId) {
        repository.findWatchRecords(projectId).forEach(watchRecord -> {
            List<Watch> watches = cache.get(watchRecord.getEntity().getIRI());
            if(watches == null) {
                watches = new ArrayList<>();
            }

            watches.add(toWatch(watchRecord));
            cache.put(watchRecord.getEntity().getIRI(), watches);
        });

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
