package edu.stanford.protege.webprotege.tag;

import edu.stanford.protege.webprotege.inject.ProjectSingleton;
import edu.stanford.protege.webprotege.persistence.Repository;
import edu.stanford.protege.webprotege.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.protege.webprotege.tag.EntityTags.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 15 Mar 2018
 */
@ProjectSingleton
public class EntityTagsRepositoryImpl implements EntityTagsRepository, Repository {

    @Nonnull
    private final MongoTemplate mongoTemplate;

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private final Lock readLock = readWriteLock.readLock();

    private final Lock writeLock = readWriteLock.writeLock();

    @Inject
    public EntityTagsRepositoryImpl(@Nonnull MongoTemplate mongoTemplate) {
        this.mongoTemplate = checkNotNull(mongoTemplate);
    }

    @PostConstruct
    @Override
    public void ensureIndexes() {
    }

    @Override
    public void save(@Nonnull EntityTags tag) {
        try {
            writeLock.lock();
            mongoTemplate.remove(tagWithProjectIdAndEntity(tag.getProjectId(), tag.getEntity()), EntityTags.class);
            mongoTemplate.save(tag);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void addTag(@Nonnull OWLEntity entity, @Nonnull TagId tagId, ProjectId projectId) {
        try {
            writeLock.lock();
            Query query = tagWithProjectIdAndEntity(projectId, entity);
            mongoTemplate.updateMulti(query, new Update().addToSet(TAGS, tagId), EntityTags.class);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void removeTag(@Nonnull OWLEntity entity, @Nonnull TagId tagId, ProjectId projectId) {
        try {
            writeLock.lock();
            Query query = tagWithProjectIdAndEntity(projectId, entity);
            mongoTemplate.updateMulti(query, new Update().pull(TAGS, tagId), EntityTags.class);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void removeTag(@Nonnull TagId tagId, ProjectId projectId) {
        try {
            writeLock.lock();
            mongoTemplate.updateMulti(tagWithProjectId(projectId), new Update().pull(TAGS, tagId), EntityTags.class);
        } finally {
            writeLock.unlock();
        }
    }

    private static Query tagWithProjectId(ProjectId projectId) {
        return query(where(PROJECT_ID).is(projectId));
    }

    private Query tagWithProjectIdAndEntity(ProjectId projectId, OWLEntity entity) {
        return query(where(PROJECT_ID).is(projectId).and(ENTITY).is(entity));
    }

    @Nonnull
    @Override
    public Optional<EntityTags> findByEntity(@Nonnull OWLEntity entity, ProjectId projectId) {
        try {
            readLock.lock();
            var query = tagWithProjectIdAndEntity(projectId, entity);
            var result = mongoTemplate.findOne(query, EntityTags.class);
            return Optional.ofNullable(result);
        } finally {
            readLock.unlock();
        }
    }

    @Nonnull
    @Override
    public Collection<EntityTags> findByTagId(@Nonnull TagId tagId, ProjectId projectId) {
        try {
            readLock.lock();
            var query = query(where(TAGS).is(tagId));
            return mongoTemplate.find(query, EntityTags.class);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public long countTaggedEntities(ProjectId projectId) {
        return mongoTemplate.count(tagWithProjectId(projectId), EntityTags.class);
    }

}
