package edu.stanford.protege.webprotege.perspective;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.ReplaceOptions;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.protege.webprotege.perspective.PerspectiveDescriptorsRecord.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-01
 */
public class PerspectiveDescriptorRepositoryImpl implements PerspectiveDescriptorRepository {

    public static final String PERSPECTIVE_DESCRIPTORS = "PerspectiveDescriptors";

    @Nonnull
    private final MongoTemplate mongoTemplate;

    @Nonnull
    private final ObjectMapper objectMapper;

    @Inject
    public PerspectiveDescriptorRepositoryImpl(@Nonnull MongoTemplate mongoTemplate, @Nonnull ObjectMapper objectMapper) {
        this.mongoTemplate = checkNotNull(mongoTemplate);
        this.objectMapper = checkNotNull(objectMapper);
    }

    @Nonnull
    private MongoCollection<Document> getCollection() {
        return mongoTemplate.getCollection(PERSPECTIVE_DESCRIPTORS);
    }

    @Override
    public void ensureIndexes() {
        var indexKeys = new Document(PROJECT_ID, 1)
                .append(USER_ID, 1)
                .append(PERSPECTIVES, 1);
        var indexOptions = new IndexOptions().unique(true);
        getCollection().createIndex(indexKeys, indexOptions);
    }

    private Document getQuery(@Nonnull PerspectiveDescriptorsRecord record) {
        var document = new Document();
        String projectId;
        if(record.getProjectId() == null) {
            projectId = null;
        }
        else {
            projectId = record.getProjectId().id();
        }
        document.append(PROJECT_ID, projectId);

        String userId;
        if(record.getUserId() == null) {
            userId = null;
        }
        else {
            userId = record.getUserId().id();
        }
        document.append(USER_ID, userId);
        return document;
    }

    @Override
    public void saveDescriptors(@Nonnull PerspectiveDescriptorsRecord perspectiveDescriptors) {
        var collection = getCollection();
        var query = getQuery(perspectiveDescriptors);
        var replacementDocument = objectMapper.convertValue(perspectiveDescriptors, Document.class);
        collection.replaceOne(query, replacementDocument, new ReplaceOptions().upsert(true));
    }

    @Nonnull
    @Override
    public Optional<PerspectiveDescriptorsRecord> findDescriptors(@Nonnull ProjectId projectId,
                                                                  @Nonnull UserId userId) {
        var query = new Document(PROJECT_ID, projectId.id())
                .append(USER_ID, userId.id());
        return getPerspectiveDescriptorRecord(query);
    }

    @Nonnull
    @Override
    public Optional<PerspectiveDescriptorsRecord> findDescriptors(@Nonnull ProjectId projectId) {
        var query = new Document(PROJECT_ID, projectId.id())
                .append(USER_ID, null);
        return getPerspectiveDescriptorRecord(query);
    }

    @Nonnull
    @Override
    public Optional<PerspectiveDescriptorsRecord> findDescriptors() {
        var query = new Document(PROJECT_ID, null)
                .append(USER_ID, null);
        return getPerspectiveDescriptorRecord(query);
    }

    @Nonnull
    @Override
    public Stream<PerspectiveDescriptorsRecord> findProjectAndSystemDescriptors(@Nonnull ProjectId projectId) {
        var projectIdQueries = Arrays.asList(
                new Document(PROJECT_ID, null),
                new Document(PROJECT_ID, projectId.id()));
        var query = new Document(new Document("$or", projectIdQueries))
                .append(USER_ID, null);
        var resultBuilder = Stream.<PerspectiveDescriptorsRecord>builder();
        try(var cursor = getCollection().find(query)
                       .cursor()) {
            while(cursor.hasNext()) {
                var doc = cursor.next();
                var record = objectMapper.convertValue(doc, PerspectiveDescriptorsRecord.class);
                resultBuilder.add(record);
            }
        }
        return resultBuilder.build();
    }

    @Override
    public void dropAllDescriptors(@Nonnull ProjectId projectId, @Nonnull UserId userId) {
        var query = new Document(PROJECT_ID, projectId.id())
                .append(USER_ID, userId.id());
        getCollection().deleteMany(query);
    }

    private Optional<PerspectiveDescriptorsRecord> getPerspectiveDescriptorRecord(Document query) {
        var document = getCollection()
                .find(query)
                .first();
        return Optional.ofNullable(document)
                .map(doc -> objectMapper.convertValue(doc, PerspectiveDescriptorsRecord.class));
    }
}
