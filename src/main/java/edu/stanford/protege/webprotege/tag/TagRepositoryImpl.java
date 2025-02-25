package edu.stanford.protege.webprotege.tag;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.ReplaceOneModel;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.WriteModel;
import edu.stanford.protege.webprotege.inject.ProjectSingleton;
import edu.stanford.protege.webprotege.persistence.Repository;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.Spliterator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21 Jun 2018
 */
@ProjectSingleton
public class TagRepositoryImpl implements TagRepository, Repository {

    private static final String COLLECTION_NAME = "Tags";

    @Nonnull
    private final MongoTemplate database;

    @Nonnull
    private final ObjectMapper objectMapper;

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private final Lock readLock = readWriteLock.readLock();

    private final Lock writeLock = readWriteLock.writeLock();

    @Inject
    public TagRepositoryImpl(@Nonnull MongoTemplate database,
                             @Nonnull ObjectMapper objectMapper) {
        this.database = checkNotNull(database);
        this.objectMapper = checkNotNull(objectMapper);
    }

    private static Document toFilter(@Nonnull TagId tagId) {
        return new Document("_id", tagId.getId());
    }

    private static Document toFilter(@Nonnull Tag tag) {
        return toFilter(tag.getTagId());
    }

    private static ReplaceOptions upsert() {
        return new ReplaceOptions().upsert(true);
    }

    @Override
    public void ensureIndexes() {
        Document index = new Document();
        index.append(Tag.PROJECT_ID, 1);
        index.append(Tag.LABEL, 1);
        getCollection().createIndex(index, new IndexOptions().unique(true));
    }

    public MongoCollection<Document> getCollection() {
        return database.getCollection(COLLECTION_NAME);
    }

    public void saveTag(@Nonnull Tag tag) {
        writeLock.lock();
        try {
            checkNotNull(tag);
            Document document = toDocument(tag);
            getCollection().replaceOne(toFilter(tag), document, upsert());
        } finally {
            writeLock.unlock();
        }

    }

    private Document toDocument(@Nonnull Tag tag) {
        return objectMapper.convertValue(tag, Document.class);
    }

    public void saveTags(@Nonnull Iterable<Tag> tags) {
        writeLock.lock();
        try {
            checkNotNull(tags);
            Spliterator<Tag> spliterator = tags.spliterator();
            List<WriteModel<Document>> updates = stream(spliterator, false)
                    .map(tag -> new ReplaceOneModel<>(
                            toFilter(tag),
                            toDocument(tag),
                            upsert()
                    ))
                    .collect(toList());
            BulkWriteResult bulkWriteResult = getCollection().bulkWrite(updates);
        } finally {
            writeLock.unlock();
        }

    }

    public void deleteTag(@Nonnull TagId tagId) {
        writeLock.lock();
        try {
            checkNotNull(tagId);
            getCollection().deleteOne(new Document("_id", tagId.getId()));
        } finally {
            writeLock.unlock();
        }

    }

    @Nonnull
    public List<Tag> findTags(ProjectId projectId) {
        readLock.lock();
        try {
            Document filter = new Document(Tag.PROJECT_ID, projectId.id());
            FindIterable<Document> documents = getCollection().find(filter);
            Stream<Document> docs = stream(documents.spliterator(), false);
            return docs.map(doc -> objectMapper.convertValue(doc, Tag.class))
                       .collect(toImmutableList());
        } finally {
            readLock.unlock();
        }

    }

    @Nonnull
    public Optional<Tag> findTagByTagId(@Nonnull TagId tagId) {
        readLock.lock();
        try {
            Document document = getCollection().find(toFilter(tagId))
                                               .limit(1)
                                               .first();
            return Optional.ofNullable(document)
                           .map(doc -> objectMapper.convertValue(doc, Tag.class));
        } finally {
            readLock.unlock();
        }

    }
}
