package edu.stanford.protege.webprotege.hierarchy.ordering;

import com.mongodb.client.model.*;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.inject.ProjectSingleton;
import edu.stanford.protege.webprotege.locking.ReadWriteLockService;
import org.bson.Document;
import org.slf4j.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.*;

import java.util.*;
import java.util.stream.Collectors;

import static edu.stanford.protege.webprotege.hierarchy.ordering.ProjectOrderedChildren.*;

@ProjectSingleton
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
            var collection = mongoTemplate.getCollection(ProjectOrderedChildren.ORDERED_CHILDREN_COLLECTION);
            collection.bulkWrite(listOfUpdateOneModelDocument, new BulkWriteOptions().ordered(false));
        });
    }

    @Override
    public Set<String> findExistingEntries(List<ProjectOrderedChildren> childrenToCheck) {
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
                () -> mongoTemplate.find(query, Document.class, ProjectOrderedChildren.ORDERED_CHILDREN_COLLECTION)
        );


        return results.stream()
                .map(doc -> doc.getString(ENTITY_URI) + "|" + doc.getString(PROJECT_ID))
                .collect(Collectors.toSet());
    }


    @Override
    public Optional<ProjectOrderedChildren> findOrderedChildren(ProjectId projectId, String entityUri) {
        Query query = new Query();
        query.addCriteria(Criteria.where(PROJECT_ID).is(projectId.id()).and(ENTITY_URI).is(entityUri));

        return readWriteLock.executeReadLock(() -> Optional.ofNullable(mongoTemplate.findOne(query, ProjectOrderedChildren.class)));
    }

    @Override
    public void insert(ProjectOrderedChildren projectOrderedChildren) {
        readWriteLock.executeWriteLock(() -> mongoTemplate.insert(projectOrderedChildren, ORDERED_CHILDREN_COLLECTION));
    }

    @Override
    public void update(ProjectOrderedChildren updatedEntry) {
        Query query = new Query();
        query.addCriteria(Criteria.where(PROJECT_ID).is(updatedEntry.projectId().id())
                .and(ENTITY_URI).is(updatedEntry.entityUri()));

        Update update = new Update()
                .set(CHILDREN, updatedEntry.children())
                .set(USER_ID, updatedEntry.userId());

        readWriteLock.executeWriteLock(() -> mongoTemplate.updateFirst(query, update, ORDERED_CHILDREN_COLLECTION));
    }


    @Override
    public void delete(ProjectOrderedChildren projectOrderedChildren) {
        readWriteLock.executeWriteLock(() -> {
            Query query = new Query();
            query.addCriteria(Criteria.where(PROJECT_ID).is(projectOrderedChildren.projectId().id())
                    .and(ENTITY_URI).is(projectOrderedChildren.entityUri()));

            mongoTemplate.remove(query, ProjectOrderedChildren.class, ORDERED_CHILDREN_COLLECTION);
        });
    }

}
