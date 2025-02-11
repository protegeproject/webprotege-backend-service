package edu.stanford.protege.webprotege.hierarchy.ordering;

import com.mongodb.client.model.BulkWriteOptions;
import com.mongodb.client.model.UpdateOneModel;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.locking.ReadWriteLockService;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static edu.stanford.protege.webprotege.hierarchy.ordering.ProjectOrderedChildren.*;

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
            var collection = mongoTemplate.getCollection(ProjectOrderedChildren.ORDERED_CHILDREN_COLLECTION);
            collection.bulkWrite(listOfUpdateOneModelDocument, new BulkWriteOptions().ordered(false));
        });
    }

    @Override
    public Set<String> findExistingEntries(List<ProjectOrderedChildren> childrenToCheck) {
        Query query = new Query();

        List<Criteria> criteriaList = childrenToCheck.stream()
                .map(child -> Criteria.where(PARENT_URI).is(child.parentUri())
                        .and(ENTITY_URI).is(child.entityUri())
                        .and(PROJECT_ID).is(child.projectId().id()))
                .toList();

        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().orOperator(criteriaList.toArray(new Criteria[0])));
        }

        query.fields().include(PARENT_URI, ENTITY_URI, PROJECT_ID);

        List<Document> results = readWriteLock.executeReadLock(
                () -> mongoTemplate.find(query, Document.class, ProjectOrderedChildren.ORDERED_CHILDREN_COLLECTION)
        );


        return results.stream()
                .map(doc -> doc.getString(PARENT_URI) + "|" + doc.getString(ENTITY_URI) + "|" + doc.getString(PROJECT_ID))
                .collect(Collectors.toSet());
    }


    @Override
    public List<ProjectOrderedChildren> findOrderedChildren(ProjectId projectId, String parentUri) {
        Query query = new Query();
        query.addCriteria(Criteria.where(ProjectOrderedChildren.PROJECT_ID).is(projectId.id())
                .and(ProjectOrderedChildren.PARENT_URI).is(parentUri));

        return readWriteLock.executeReadLock(() -> mongoTemplate.find(query, ProjectOrderedChildren.class));
    }


}
