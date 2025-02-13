package edu.stanford.protege.webprotege.perspective;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.ReplaceOneModel;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.WriteModel;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.protege.webprotege.perspective.PerspectiveLayoutRecord.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-01
 */
public class PerspectiveLayoutRepositoryImpl implements PerspectiveLayoutRepository {

    public static final String PERSPECTIVE_LAYOUTS = "PerspectiveLayouts";

    private static final Logger logger = LoggerFactory.getLogger(PerspectiveLayoutRepositoryImpl.class);

    @Nonnull
    private final MongoTemplate mongoTemplate;

    @Nonnull
    private final ObjectMapper objectMapper;

    @Inject
    public PerspectiveLayoutRepositoryImpl(@Nonnull MongoTemplate mongoTemplate, @Nonnull ObjectMapper objectMapper) {
        this.mongoTemplate = checkNotNull(mongoTemplate);
        this.objectMapper = checkNotNull(objectMapper);
    }

    @Nonnull
    private MongoCollection<Document> getCollection() {
        return mongoTemplate.getCollection(PERSPECTIVE_LAYOUTS);
    }

    private Document getQuery(@Nullable String projectId,
                              @Nullable String userId,
                              @Nonnull PerspectiveId perspectiveId) {
        return new Document(PROJECT_ID, projectId)
                .append(USER_ID, userId)
                .append(PERSPECTIVE_ID, perspectiveId.getId());
    }

    @Nonnull
    @Override
    public Optional<PerspectiveLayoutRecord> findLayout(@Nonnull ProjectId projectId,
                                                        @Nonnull UserId userId,
                                                        @Nonnull PerspectiveId perspectiveId) {
        var query = getQuery(projectId.id(), userId.id(), perspectiveId);
        return findLayout(query);
    }

    private Optional<PerspectiveLayoutRecord> findLayout(Document query) {
        var document = getCollection().find(query)
                .first();
        if(document == null) {
            return Optional.empty();
        }
        else {
            var record = objectMapper.convertValue(document, PerspectiveLayoutRecord.class);
            return Optional.of(record);
        }
    }

    @Nonnull
    @Override
    public Optional<PerspectiveLayoutRecord> findLayout(@Nonnull ProjectId projectId,
                                                        @Nonnull PerspectiveId perspectiveId) {
        var query = getQuery(projectId.id(), null, perspectiveId);
        return findLayout(query);
    }

    @Nonnull
    @Override
    public Optional<PerspectiveLayoutRecord> findLayout(@Nonnull PerspectiveId perspectiveId) {
        var query = getQuery(null, null, perspectiveId);
        return findLayout(query);
    }

    @Override
    public void saveLayout(PerspectiveLayoutRecord record) {
        saveLayouts(ImmutableList.of(record));
    }

    private Document getQuery(PerspectiveLayoutRecord record) {
        return getQuery(Optional.ofNullable(record.getProjectId()).map(ProjectId::id).orElse(null),
                        Optional.ofNullable(record.getUserId()).map(UserId::id).orElse(null),
                        record.getPerspectiveId());
    }

    @Override
    public void saveLayouts(@Nonnull List<PerspectiveLayoutRecord> records) {
        try {
            var writes = new ArrayList<WriteModel<Document>>(records.size());
            for(var record : records) {
                var query = getQuery(record);
                var nextDocument = objectMapper.convertValue(record, Document.class);
                var write = new ReplaceOneModel<>(query, nextDocument, new ReplaceOptions().upsert(true));
                writes.add(write);
            }
            getCollection().bulkWrite(writes);
        } catch (MongoException e) {
            logger.error("An error occurred when saving the perspective layouts", e);
        }
    }

    @Override
    public void dropLayout(@Nonnull ProjectId projectId, @Nonnull UserId userId, @Nonnull PerspectiveId perspectiveId) {
        try {
            var query = getQuery(projectId.id(), userId.id(), perspectiveId);
            getCollection().deleteOne(query);
        } catch (MongoException e) {
            logger.error("An error occurred when dropping the perspective layout for a user", e);
        }
    }

    @Override
    public void dropAllLayouts(@Nonnull ProjectId projectId, @Nonnull UserId userId) {
        try {
            var query = new Document(PROJECT_ID, projectId.id())
                    .append(USER_ID, userId.id());
            getCollection().deleteMany(query);
        } catch (MongoException e) {
            logger.error("An error occurred when dropping all project layouts for a user", e);
        }
    }

    @Override
    public void ensureIndexes() {
        var indexKeys = new Document(PROJECT_ID, 1)
                .append(USER_ID, 1)
                .append(PERSPECTIVE_ID, 1);
        var indexOptions = new IndexOptions().unique(true);
        getCollection()
                .createIndex(indexKeys, indexOptions);
    }
}
