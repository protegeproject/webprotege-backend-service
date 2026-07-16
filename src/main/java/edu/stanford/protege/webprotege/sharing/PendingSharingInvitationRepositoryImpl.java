package edu.stanford.protege.webprotege.sharing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.ErrorCategory;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.ReplaceOptions;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.inject.ApplicationSingleton;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.protege.webprotege.sharing.PendingSharingInvitation.PERSON_KEY;
import static edu.stanford.protege.webprotege.sharing.PendingSharingInvitation.PROJECT_ID;

/**
 * Mongo-backed {@link PendingSharingInvitationRepository}.
 */
@ApplicationSingleton
public class PendingSharingInvitationRepositoryImpl implements PendingSharingInvitationRepository {

    private static final String COLLECTION_NAME = "PendingSharingInvitations";

    private static final String $NIN = "$nin";

    private static final Logger logger = LoggerFactory.getLogger(PendingSharingInvitationRepositoryImpl.class);

    private final MongoTemplate database;

    private final ObjectMapper objectMapper;

    @Inject
    public PendingSharingInvitationRepositoryImpl(@Nonnull MongoTemplate database,
                                                  @Nonnull ObjectMapper objectMapper) {
        this.database = checkNotNull(database);
        this.objectMapper = checkNotNull(objectMapper);
    }

    @Override
    public void ensureIndexes() {
        var collection = getCollection();
        collection.createIndex(new Document(PROJECT_ID, 1).append(PERSON_KEY, 1),
                               new IndexOptions().unique(true));
        collection.createIndex(new Document(PERSON_KEY, 1));
    }

    @Override
    public boolean upsert(@Nonnull PendingSharingInvitation invitation) {
        return upsert(invitation, true);
    }

    private boolean upsert(@Nonnull PendingSharingInvitation invitation, boolean retryOnDuplicateKey) {
        var filter = projectAndKeyFilter(invitation.projectId(), invitation.personKey());
        var toStore = mergeWithExisting(invitation, filter);
        var document = objectMapper.convertValue(toStore, Document.class);
        try {
            var result = getCollection().replaceOne(filter, document, new ReplaceOptions().upsert(true));
            return result.getUpsertedId() != null;
        } catch (MongoWriteException e) {
            // A concurrent upsert of the same (projectId, personKey) can lose the race to insert and
            // hit the unique index. Retry exactly once: the winning insert now exists, so the retry
            // resolves to a plain replace rather than an insert.
            if (retryOnDuplicateKey && e.getError().getCategory() == ErrorCategory.DUPLICATE_KEY) {
                return upsert(invitation, false);
            }
            throw e;
        }
    }

    /**
     * Preserves the original {@code createdAt} and {@code invitedBy} of an existing invitation so a
     * re-save updates only the permission, leaving the invitation's identity and age intact.
     */
    private PendingSharingInvitation mergeWithExisting(PendingSharingInvitation invitation, Document filter) {
        var existingDocument = getCollection().find(filter).first();
        if (existingDocument == null) {
            return invitation;
        }
        try {
            var existing = objectMapper.convertValue(existingDocument, PendingSharingInvitation.class);
            return new PendingSharingInvitation(invitation.projectId(),
                                                invitation.personId(),
                                                invitation.personKey(),
                                                invitation.sharingPermission(),
                                                existing.invitedBy(),
                                                existing.createdAt());
        } catch (RuntimeException e) {
            logger.warn("Could not read the existing pending sharing invitation while upserting - " +
                        "replacing it wholesale: {}", e.toString());
            return invitation;
        }
    }

    @Nonnull
    @Override
    public List<PendingSharingInvitation> findByPersonKeys(@Nonnull Collection<String> personKeys) {
        // Tolerant read: skip (and warn about) any single corrupt document. This drives login-time
        // redemption, where one unconvertable invitation must not block a user from redeeming all of
        // their other invitations.
        if (personKeys.isEmpty()) {
            return new ArrayList<>();
        }
        return read(new Document(PERSON_KEY, new Document("$in", new ArrayList<>(personKeys))), true);
    }

    @Nonnull
    @Override
    public List<PendingSharingInvitation> findByProjectId(@Nonnull ProjectId projectId) {
        // Strict read: a per-document conversion failure propagates. This drives the sharing-page load
        // and its merge; silently dropping a corrupt invitation would return a partial list that the
        // next save's reconciliation would then interpret as removals of the dropped invitations.
        return read(new Document(PROJECT_ID, projectId.id()), false);
    }

    @Override
    public void deleteByProjectIdWherePersonKeyNotIn(@Nonnull ProjectId projectId,
                                                     @Nonnull Collection<String> personKeysToKeep) {
        var filter = new Document(PROJECT_ID, projectId.id())
                .append(PERSON_KEY, new Document($NIN, new ArrayList<>(personKeysToKeep)));
        getCollection().deleteMany(filter);
    }

    @Override
    public long delete(@Nonnull ProjectId projectId, @Nonnull String personKey) {
        var result = getCollection().deleteOne(projectAndKeyFilter(projectId, personKey));
        return result.getDeletedCount();
    }

    /**
     * Runs a query and converts each matching document. A failure reaching the database always
     * propagates. A per-document conversion failure is skipped (with a warning) when {@code tolerant}
     * is true, or propagates when it is false - see the two callers for why they differ.
     */
    private List<PendingSharingInvitation> read(Document filter, boolean tolerant) {
        var result = new ArrayList<PendingSharingInvitation>();
        try (var cursor = getCollection().find(filter).cursor()) {
            while (cursor.hasNext()) {
                var document = cursor.next();
                if (tolerant) {
                    try {
                        result.add(objectMapper.convertValue(document, PendingSharingInvitation.class));
                    } catch (RuntimeException e) {
                        logger.warn("Skipping a pending sharing invitation document that could not be " +
                                    "converted: {}", e.toString());
                    }
                } else {
                    result.add(objectMapper.convertValue(document, PendingSharingInvitation.class));
                }
            }
        }
        return result;
    }

    private static Document projectAndKeyFilter(ProjectId projectId, String personKey) {
        return new Document(PROJECT_ID, projectId.id()).append(PERSON_KEY, personKey);
    }

    private MongoCollection<Document> getCollection() {
        return database.getCollection(COLLECTION_NAME);
    }
}
