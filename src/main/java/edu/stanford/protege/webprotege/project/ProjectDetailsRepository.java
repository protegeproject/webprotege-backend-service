package edu.stanford.protege.webprotege.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.Updates;
import edu.stanford.protege.webprotege.api.TimestampSerializer;
import edu.stanford.protege.webprotege.common.DictionaryLanguage;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.inject.ApplicationSingleton;
import edu.stanford.protege.webprotege.persistence.Repository;
import edu.stanford.protege.webprotege.common.UserId;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;
import static edu.stanford.protege.webprotege.project.ProjectDetails.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11/03/16
 */
@ApplicationSingleton
public class ProjectDetailsRepository implements Repository {

    public static final String COLLECTION_NAME = "ProjectDetails";

    private static final long MAX_CACHE_SIZE = 2000;

    @Nonnull
    private final ObjectMapper objectMapper;

    private final MongoCollection<Document> collection;

    private final LoadingCache<ProjectId, ProjectDetails> cache;

    private final LoadingCache<ProjectId, ImmutableList<DictionaryLanguage>> displayLanguagesCache;

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private final Lock readLock = readWriteLock.readLock();

    private final Lock writeLock = readWriteLock.writeLock();

    @Inject
    public ProjectDetailsRepository(@Nonnull MongoTemplate mongoTemplate,
                                    @Nonnull ObjectMapper objectMapper) {
        this.collection = mongoTemplate.getCollection(COLLECTION_NAME);
        this.objectMapper = checkNotNull(objectMapper);
        this.cache = Caffeine
                .newBuilder()
                .maximumSize(MAX_CACHE_SIZE)
                .build(this::findOneFromDbOrNull);
        this.displayLanguagesCache = Caffeine
                .newBuilder()
                .maximumSize(MAX_CACHE_SIZE)
                .build(this::findDisplayLanguagesForProject);
    }

    private ImmutableList<DictionaryLanguage> findDisplayLanguagesForProject(@NonNull ProjectId projectId) {
        // Find the project details
        return findOne(projectId)
                // Map the project details into a list of dictionary languages
                .map(details -> details
                        .getDefaultDisplayNameSettings()
                        .getPrimaryDisplayNameLanguages()
                        .stream()
                        .collect(toImmutableList()))
                .orElse(ImmutableList.of());
    }

    /**
     * Find project details from the database.  If the project details are not found
     * then null is returned.
     * @param projectId The project id for the project details to be retrieved
     */
    @Nullable
    private ProjectDetails findOneFromDbOrNull(@NonNull ProjectId projectId) {
        return findOneFromDb(projectId).orElse(null);
    }

    private Optional<ProjectDetails> findOneFromDb(@Nonnull ProjectId projectId) {
        return Optional
                .ofNullable(collection.find(withProjectId(projectId))
                                    .limit(1)
                                    .first())
                .map(d -> objectMapper.convertValue(d, ProjectDetails.class));
    }

    public Optional<ProjectDetails> findOne(@Nonnull ProjectId projectId) {
        try {
            readLock.lock();
            return Optional.ofNullable(cache.get(projectId));
        } finally {
            readLock.unlock();
        }
    }

    private static Document withProjectId(@Nonnull ProjectId projectId) {
        return new Document(PROJECT_ID, projectId.id());
    }

    @Override
    public void ensureIndexes() {
        collection.createIndex(new Document(PROJECT_ID, 1).append(DISPLAY_NAME, 1));
    }

    public boolean containsProject(@Nonnull ProjectId projectId) {
        try {
            readLock.lock();
            return collection.find(withProjectId(projectId)).projection(new Document()).limit(1).first() != null;
        } finally {
            readLock.unlock();
        }
    }

    public boolean containsProjectWithOwner(@Nonnull ProjectId projectId,
                                            @Nonnull UserId owner) {
        try {
            readLock.lock();
            return collection
                    .find(withProjectIdAndWithOwner(projectId, owner))
                    .projection(new Document())
                    .limit(1)
                    .first() != null;
        } finally {
            readLock.unlock();
        }
    }

    private static Document withProjectIdAndWithOwner(@Nonnull ProjectId projectId,
                                                      @Nonnull UserId owner) {
        return new Document(PROJECT_ID, projectId.id()).append(OWNER, owner.id());
    }

    public void setInTrash(ProjectId projectId,
                           boolean inTrash) {
        try {
            writeLock.lock();
            collection.updateOne(withProjectId(projectId), updateInTrash(inTrash));
            cache.invalidate(projectId);
            cache.get(projectId);
            // No need to invalidate display languages cache
        } finally {
            writeLock.unlock();
        }
    }

    public static Bson updateInTrash(boolean inTrash) {
        return Updates.set(IN_TRASH, inTrash);
    }

    public void setModified(ProjectId projectId,
                            long modifiedAt,
                            UserId modifiedBy) {
        try {
            writeLock.lock();
            collection.updateOne(withProjectId(projectId), updateModified(modifiedBy, modifiedAt));
            cache.invalidate(projectId);
            cache.get(projectId);
            // No need to invalidate display languages cache
        } finally {
            writeLock.unlock();
        }
    }

    public static Bson updateModified(UserId userId,
                                      long timestamp) {
        return Updates.combine(Updates.set(MODIFIED_AT, TimestampSerializer.toIsoDateTime(timestamp)), Updates.set(MODIFIED_BY, userId
                .id()));
    }

    public List<ProjectDetails> findByOwner(UserId owner) {
        try {
            readLock.lock();
            ArrayList<ProjectDetails> result = new ArrayList<>();
            collection.find(withOwner(owner)).map(d -> objectMapper.convertValue(d, ProjectDetails.class)).into(result);
            return result;
        } finally {
            readLock.unlock();
        }
    }

    private static Document withOwner(@Nonnull UserId owner) {
        return new Document(OWNER, owner.id());
    }

    public void save(@Nonnull ProjectDetails projectRecord) {
        try {
            writeLock.lock();
            var document = objectMapper.convertValue(projectRecord, Document.class);
            var projectId = projectRecord.projectId();
            collection.replaceOne(withProjectId(projectId), document, new ReplaceOptions().upsert(true));
            cache.invalidate(projectId);
            cache.get(projectId);
            displayLanguagesCache.invalidate(projectId);
        } finally {
            writeLock.unlock();
        }
    }

    public void delete(@Nonnull ProjectId projectId) {
        try {
            writeLock.lock();
            collection.deleteOne(withProjectId(projectId));
            cache.invalidate(projectId);
            cache.get(projectId);
            displayLanguagesCache.invalidate(projectId);
        } finally {
            writeLock.unlock();
        }
    }

    public ImmutableList<DictionaryLanguage> getDisplayNameLanguages(@Nonnull ProjectId projectId) {
        try {
            readLock.lock();
            return displayLanguagesCache.get(projectId);
        } finally {
            readLock.unlock();
        }
    }
}
