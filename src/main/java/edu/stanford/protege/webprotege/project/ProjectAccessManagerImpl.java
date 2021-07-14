package edu.stanford.protege.webprotege.project;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOptions;
import edu.stanford.protege.webprotege.persistence.Repository;
import edu.stanford.protege.webprotege.user.UserId;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Date;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 3 Mar 2017
 */
public class ProjectAccessManagerImpl implements ProjectAccessManager, Repository {

    public static final String COLLECTION_NAME = "ProjectAccess";

    public static final String PROJECT_ID = "projectId";

    public static final String USER_ID = "userId";

    public static final String ACCESSED = "accessed";

    private final MongoCollection<Document> collection;

    @Inject
    public ProjectAccessManagerImpl(@Nonnull MongoTemplate mongoTemplate) {
        this.collection = mongoTemplate.getCollection(COLLECTION_NAME);
    }

    @Override
    public void ensureIndexes() {
        collection.createIndex(new Document()
                                       .append(PROJECT_ID, 1)
                                       .append(USER_ID, 1));
    }

    @Override
    public void logProjectAccess(ProjectId projectId, UserId userId, long timestamp) {
        collection.updateOne(
                and(eq(PROJECT_ID, projectId.getId()),
                    eq(USER_ID, userId.getUserName())),
                new Document()
                        .append("$inc", new Document("count", 1))
                        .append("$set", new Document(ACCESSED, new Date(timestamp))),
                new UpdateOptions().upsert(true)
        );
    }
}
