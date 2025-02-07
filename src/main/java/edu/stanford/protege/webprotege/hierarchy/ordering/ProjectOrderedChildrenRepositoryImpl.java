package edu.stanford.protege.webprotege.hierarchy.ordering;

import com.mongodb.client.model.InsertOneModel;
import edu.stanford.protege.webprotege.locking.ReadWriteLockService;
import org.bson.Document;
import org.slf4j.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

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
    public void bulkWriteDocuments(List<InsertOneModel<Document>> listOfInsertOneModelDocument) {
        readWriteLock.executeWriteLock(() -> {
            var collection = mongoTemplate.getCollection(ProjectOrderedChildren.ORDERED_CHILDREN_COLLECTION);
            collection.bulkWrite(listOfInsertOneModelDocument);
        });
    }
}
