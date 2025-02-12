package edu.stanford.protege.webprotege.hierarchy.ordering;

import com.mongodb.client.model.BulkWriteOptions;
import com.mongodb.client.model.UpdateOneModel;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.locking.ReadWriteLockService;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

import static edu.stanford.protege.webprotege.hierarchy.ordering.EntityChildrenOrdering.*;

@Repository
public class ProjectOrderedChildrenRepositoryImpl implements ProjectOrderedChildrenRepository {

    private final static Logger LOGGER = LoggerFactory.getLogger(ProjectOrderedChildrenRepositoryImpl.class);
    private final MongoTemplate mongoTemplate;
    private final ReadWriteLockService readWriteLock;

    public ProjectOrderedChildrenRepositoryImpl(MongoTemplate mongoTemplate,
                                                ReadWriteLockService readWriteLock) {
        this.mongoTemplate = mongoTemplate;
        this.readWriteLock = readWriteLock;
    }

    @Override
    public void bulkWriteDocuments(List<UpdateOneModel<Document>> listOfUpdateOneModelDocument) {
        readWriteLock.executeWriteLock(() -> {
            var collection = mongoTemplate.getCollection(EntityChildrenOrdering.ORDERED_CHILDREN_COLLECTION);
            collection.bulkWrite(listOfUpdateOneModelDocument, new BulkWriteOptions().ordered(false));
        });
    }

    @Override
    public Set<String> findExistingEntries(List<EntityChildrenOrdering> childrenToCheck) {
        Query query = new Query();

        List<Criteria> criteriaList = childrenToCheck.stream()
                .map(entry -> Criteria.where(ENTITY_URI).is(entry.entityUri())
                        .and(PROJECT_ID).is(entry.projectId().id()))
                .toList();

        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().orOperator(criteriaList.toArray(new Criteria[0])));
        }

        query.fields().include(ENTITY_URI, PROJECT_ID);

        List<Document> results = readWriteLock.executeReadLock(
                () -> mongoTemplate.find(query, Document.class, EntityChildrenOrdering.ORDERED_CHILDREN_COLLECTION)
        );


        return results.stream()
                .map(doc -> doc.getString(ENTITY_URI) + "|" + doc.getString(PROJECT_ID))
                .collect(Collectors.toSet());
    }


    @Override
    public Optional<EntityChildrenOrdering> findOrderedChildren(ProjectId projectId, String entityUri) {
        Query query = new Query();
        query.addCriteria(Criteria.where(PROJECT_ID).is(projectId.id()).and(ENTITY_URI).is(entityUri));

        return readWriteLock.executeReadLock(() -> Optional.ofNullable(mongoTemplate.findOne(query, EntityChildrenOrdering.class)));
    }

    @Override
    public void save(EntityChildrenOrdering projectOrderedChildren) {
        readWriteLock.executeWriteLock(() -> mongoTemplate.save(projectOrderedChildren, ORDERED_CHILDREN_COLLECTION));
    }

    @Override
    public void update(EntityChildrenOrdering updatedEntry) {
        Query query = new Query();
        query.addCriteria(Criteria.where(PROJECT_ID).is(updatedEntry.projectId().id())
                .and(ENTITY_URI).is(updatedEntry.entityUri()));

        Update update = new Update()
                .set(CHILDREN, updatedEntry.children())
                .set(USER_ID, updatedEntry.userId());

        readWriteLock.executeWriteLock(() -> mongoTemplate.updateFirst(query, update, ORDERED_CHILDREN_COLLECTION));
    }


    @Override
    public void delete(EntityChildrenOrdering projectOrderedChildren) {
        readWriteLock.executeWriteLock(() -> {
            Query query = new Query();
            query.addCriteria(Criteria.where(PROJECT_ID).is(projectOrderedChildren.projectId().id())
                    .and(ENTITY_URI).is(projectOrderedChildren.entityUri()));

            mongoTemplate.remove(query, EntityChildrenOrdering.class, ORDERED_CHILDREN_COLLECTION);
        });
    }

}
