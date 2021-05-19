package edu.stanford.protege.webprotege.watches;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.ReplaceOptions;
import edu.stanford.protege.webprotege.inject.ApplicationSingleton;
import edu.stanford.protege.webprotege.project.ProjectId;
import edu.stanford.protege.webprotege.user.UserId;
import org.bson.Document;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static edu.stanford.protege.webprotege.watches.WatchRecord.*;
import static java.util.stream.Collectors.toList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Apr 2017
 */
@ApplicationSingleton
public class WatchRecordRepositoryImpl implements WatchRecordRepository {

    public static final String COLLECTION_NAME = "Watches";

    private static final String ENTITY_TYPE = "type";

    private static final String ENTITY_IRI = "iri";

    private static final String $IN = "$in";

    private final MongoDatabase database;

    private final ObjectMapper objectMapper;

    @Inject
    public WatchRecordRepositoryImpl(@Nonnull MongoDatabase database, @Nonnull ObjectMapper objectMapper) {
        this.database = database;
        this.objectMapper = objectMapper;
    }

    @Override
    public void ensureIndexes() {
        var watches = getCollection();
        var indexKeys = new Document();
        indexKeys.append(PROJECT_ID, 1).append(USER_ID, 1).append(ENTITY, 1);
        watches.createIndex(indexKeys, new IndexOptions().unique(true));
    }

    private MongoCollection<Document> getCollection() {
        return database.getCollection(COLLECTION_NAME);
    }

    /**
     * Finds {@link WatchRecord}s for the specified user.
     *
     * @param userId The user
     * @return The {@link WatchRecord}s for the specified user.
     */
    @Override
    public List<WatchRecord> findWatchRecords(@Nonnull ProjectId projectId, @Nonnull UserId userId) {
        var query = getProjectAndUserQuery(projectId, userId);
        return getWatchRecords(query);
    }

    /**
     * Finds {@link WatchRecord}s for the specified entities.
     *
     * @param entities The entities
     * @return The {@link WatchRecord}s for the specified entities. Any WatchRecord for that watches
     * one of the specified entities will be returned
     */
    @Override
    public List<WatchRecord> findWatchRecords(@Nonnull ProjectId projectId,
                                              @Nonnull Collection<? extends OWLEntity> entities) {
        var query = new Document()
                .append(PROJECT_ID, projectId.getId())
                .append(ENTITY, new Document("$in", getEntityDocuments(entities)));
        return getWatchRecords(query);
    }

    /**
     * Finds {@link WatchRecord}s for the specified user and entities.
     *
     * @param userId   The {@link UserId}.
     * @param entities The {@link OWLEntity}.
     * @return The {@link WatchRecord}s for the specified user and entities.  Each watch record will specify
     * a watch for the specified user and one of the specifed entities.
     */
    @Override
    public List<WatchRecord> findWatchRecords(@Nonnull ProjectId projectId,
                                              @Nonnull UserId userId,
                                              @Nonnull Collection<? extends OWLEntity> entities) {
        var query = getProjectAndUserQuery(projectId, userId);
        query.append(ENTITY, new Document($IN, getEntityDocuments(entities)));
        return getWatchRecords(query);
    }

    /**
     * Save a {@link WatchRecord}
     *
     * @param watch The {@link WatchRecord} to be saved.
     */
    @Override
    public void saveWatchRecord(@Nonnull WatchRecord watch) {
        var watches = getCollection();
        var findQuery = getProjectAndUserQuery(watch.getProjectId(),
                                               watch.getUserId())
                .append(ENTITY, getEntityDocument(watch.getEntity()));
        var document = convertToDocument(watch);
        watches.replaceOne(findQuery, document, new ReplaceOptions().upsert(true));
    }

    /**
     * Delete a {@link WatchRecord}.
     *
     * @param watch The {@link WatchRecord} to be deleted.
     */
    @Override
    public void deleteWatchRecord(@Nonnull WatchRecord watch) {
        var deleteQuery = getProjectAndUserQuery(watch.getProjectId(),
                               watch.getUserId())
                .append(ENTITY, getEntityDocument(watch.getEntity()))
                .append(TYPE, watch.getType().name());
        getCollection().deleteOne(deleteQuery);
    }



    private static List<Document> getEntityDocuments(@Nonnull Collection<? extends OWLEntity> entities) {
        return entities.stream().map(WatchRecordRepositoryImpl::getEntityDocument)
                       .collect(toList());
    }

    private static Document getEntityDocument(@Nonnull OWLEntity entity) {
        return new Document().append(ENTITY_TYPE, entity.getEntityType().getName())
                             .append(ENTITY_IRI, entity.getIRI().toString());
    }

    private List<WatchRecord> getWatchRecords(Document query) {
        var watches = getCollection();
        var findIterable = watches.find(query);
        var result = new ArrayList<WatchRecord>();
        try (var cursor = findIterable.cursor()) {
            while (cursor.hasNext()) {
                var watchRecordDoc = cursor.next();
                var watchRecord = objectMapper.convertValue(watchRecordDoc, WatchRecord.class);
                result.add(watchRecord);
            }
        }
        return result;
    }

    private static Document getProjectAndUserQuery(@Nonnull ProjectId projectId, @Nonnull UserId userId) {
        return new Document().append(PROJECT_ID, projectId.getId()).append(USER_ID, userId.getUserName());
    }


    /**
     * Convert a {@link WatchRecord} to a {@link Document}.  We can't just use the {@link ObjectMapper}
     * because of the legacy format for entities in the Watches DB.
     */
    private static Document convertToDocument(@Nonnull WatchRecord watch) {
        return new Document()
                .append(PROJECT_ID, watch.getProjectId().getId())
                .append(USER_ID, watch.getUserId().getUserName())
                .append(ENTITY, getEntityDocument(watch.getEntity()))
                .append(TYPE, watch.getType().name());
    }
}
