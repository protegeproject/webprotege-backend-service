package edu.stanford.protege.webprotege.user;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.ReplaceOptions;
import edu.stanford.protege.webprotege.common.UserId;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.protege.webprotege.user.UserRecordConverter.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 09/03/16
 */
public class UserRecordRepository {

    private static final String COLLECTION_NAME = "Users";

    private UserRecordConverter converter = new UserRecordConverter();

    @Nonnull
    private final MongoCollection<Document> collection;

    @Inject
    public UserRecordRepository(@Nonnull MongoTemplate mongoTemplate,
                                @Nonnull UserRecordConverter converter) {
        this.collection = checkNotNull(mongoTemplate).getCollection(COLLECTION_NAME);
        this.converter = checkNotNull(converter);
    }

    public List<UserId> findByUserIdContainingIgnoreCase(String match, int limit) {
        FindIterable<Document> documents = collection
                .find(byUserIdContainsIgnoreCase(match))
                .projection(withUserId())
                .limit(limit);
        List<UserId> result = new ArrayList<>(limit);
        documents.map(d -> converter.getUserId(d)).into(result);
        return result;
    }

    public Optional<UserRecord> findOne(UserId userId) {
        Document firstDocument = collection
                .find(byUserId(userId))
                .limit(1)
                .first();
        return Optional.ofNullable(firstDocument).map(d -> converter.fromDocument(d));
    }

    public Optional<UserRecord> findOneByEmailAddress(String emailAddress) {
        Document firstDocument = collection
                .find(byEmailAddress(emailAddress))
                .limit(1)
                .first();
        return Optional.ofNullable(firstDocument).map(d -> converter.fromDocument(d));
    }

    public void save(UserRecord userRecord) {
        Document document = converter.toDocument(userRecord);
        collection.replaceOne(byUserId(userRecord.getUserId()), document, new ReplaceOptions().upsert(true));
    }

    public void delete(UserId userId) {
        collection.deleteOne(byUserId(userId));
    }
}
