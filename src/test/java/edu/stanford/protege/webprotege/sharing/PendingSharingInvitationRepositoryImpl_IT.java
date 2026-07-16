package edu.stanford.protege.webprotege.sharing;

import com.mongodb.ErrorCategory;
import com.mongodb.MongoWriteException;
import edu.stanford.protege.webprotege.MongoTestExtension;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;
import org.bson.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.annotation.DirtiesContext;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest(properties = {"webprotege.rabbitmq.commands-subscribe=false"})
@ExtendWith({MongoTestExtension.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PendingSharingInvitationRepositoryImpl_IT {

    private static final String COLLECTION_NAME = "PendingSharingInvitations";

    @Autowired
    private PendingSharingInvitationRepository repository;

    @Autowired
    private MongoTemplate mongoTemplate;

    private final ProjectId projectId = ProjectId.valueOf("12345678-1234-1234-1234-123456789abc");

    private final ProjectId otherProjectId = ProjectId.valueOf("12345678-1234-1234-1234-123456789def");

    private final UserId invitedBy = UserId.valueOf("owner");

    private final Instant createdAt = Instant.parse("2024-01-01T00:00:00Z");

    @BeforeEach
    public void setUp() {
        repository.ensureIndexes();
    }

    @AfterEach
    public void tearDown() {
        mongoTemplate.getCollection(COLLECTION_NAME).drop();
    }

    private PendingSharingInvitation invitation(ProjectId projectId,
                                                String personId,
                                                SharingPermission permission,
                                                UserId invitedBy,
                                                Instant createdAt) {
        return new PendingSharingInvitation(projectId, personId, personId, permission, invitedBy, createdAt);
    }

    @Test
    public void shouldReportInsertOnFirstUpsertAndReplaceOnSecond() {
        assertThat(repository.upsert(invitation(projectId, "new@x.org", SharingPermission.VIEW, invitedBy, createdAt)),
                   is(true));
        assertThat(mongoTemplate.getCollection(COLLECTION_NAME).countDocuments(), is(1L));

        assertThat(repository.upsert(invitation(projectId, "new@x.org", SharingPermission.EDIT, invitedBy, createdAt)),
                   is(false));
        assertThat(mongoTemplate.getCollection(COLLECTION_NAME).countDocuments(), is(1L));
    }

    @Test
    public void shouldPreserveOriginalCreatedAtAndInvitedByWhenReplacing() {
        repository.upsert(invitation(projectId, "new@x.org", SharingPermission.VIEW, invitedBy, createdAt));

        Instant laterCreatedAt = Instant.parse("2025-01-01T00:00:00Z");
        UserId laterInviter = UserId.valueOf("someone-else");
        repository.upsert(invitation(projectId, "new@x.org", SharingPermission.EDIT, laterInviter, laterCreatedAt));

        List<PendingSharingInvitation> found = repository.findByProjectId(projectId);
        assertThat(found, hasSize(1));
        PendingSharingInvitation invitation = found.get(0);
        // The permission is updated, but the original identity and age of the invitation are kept.
        assertThat(invitation.sharingPermission(), is(SharingPermission.EDIT));
        assertThat(invitation.invitedBy(), is(invitedBy));
        assertThat(invitation.createdAt(), is(createdAt));
    }

    @Test
    public void shouldFindByProjectId() {
        repository.upsert(invitation(projectId, "a@x.org", SharingPermission.VIEW, invitedBy, createdAt));
        repository.upsert(invitation(projectId, "b@x.org", SharingPermission.EDIT, invitedBy, createdAt));
        repository.upsert(invitation(otherProjectId, "c@x.org", SharingPermission.MANAGE, invitedBy, createdAt));

        assertThat(repository.findByProjectId(projectId), hasSize(2));
        assertThat(repository.findByProjectId(otherProjectId), hasSize(1));
    }

    @Test
    public void shouldFindByPersonKeys() {
        repository.upsert(invitation(projectId, "a@x.org", SharingPermission.VIEW, invitedBy, createdAt));
        repository.upsert(invitation(otherProjectId, "a@x.org", SharingPermission.EDIT, invitedBy, createdAt));
        repository.upsert(invitation(projectId, "b@x.org", SharingPermission.MANAGE, invitedBy, createdAt));

        List<PendingSharingInvitation> found = repository.findByPersonKeys(Set.of("a@x.org"));
        assertThat(found, hasSize(2));
    }

    @Test
    public void shouldReturnNothingForEmptyPersonKeys() {
        repository.upsert(invitation(projectId, "a@x.org", SharingPermission.VIEW, invitedBy, createdAt));

        assertThat(repository.findByPersonKeys(Set.of()), hasSize(0));
    }

    @Test
    public void shouldDeleteInvitationsWhosePersonKeyIsNotKept() {
        repository.upsert(invitation(projectId, "keep@x.org", SharingPermission.VIEW, invitedBy, createdAt));
        repository.upsert(invitation(projectId, "drop@x.org", SharingPermission.EDIT, invitedBy, createdAt));
        repository.upsert(invitation(otherProjectId, "other@x.org", SharingPermission.MANAGE, invitedBy, createdAt));

        repository.deleteByProjectIdWherePersonKeyNotIn(projectId, Set.of("keep@x.org"));

        List<PendingSharingInvitation> remaining = repository.findByProjectId(projectId);
        assertThat(remaining, hasSize(1));
        assertThat(remaining.get(0).personKey(), is("keep@x.org"));
        // A different project's invitations are untouched.
        assertThat(repository.findByProjectId(otherProjectId), hasSize(1));
    }

    @Test
    public void shouldDeleteAllPendingInvitationsForProjectWhenNoKeysAreKept() {
        repository.upsert(invitation(projectId, "a@x.org", SharingPermission.VIEW, invitedBy, createdAt));
        repository.upsert(invitation(projectId, "b@x.org", SharingPermission.EDIT, invitedBy, createdAt));

        repository.deleteByProjectIdWherePersonKeyNotIn(projectId, Set.of());

        assertThat(repository.findByProjectId(projectId), hasSize(0));
    }

    @Test
    public void shouldReportDeletedCountAsAClaim() {
        repository.upsert(invitation(projectId, "new@x.org", SharingPermission.VIEW, invitedBy, createdAt));

        assertThat(repository.delete(projectId, "new@x.org"), is(1L));
        // A second delete of the same invitation is a no-op - the claim has already been taken.
        assertThat(repository.delete(projectId, "new@x.org"), is(0L));
    }

    @Test
    public void findByProjectIdShouldPropagateAConversionFailureWhileFindByPersonKeysSkipsIt() {
        repository.upsert(invitation(projectId, "good@x.org", SharingPermission.VIEW, invitedBy, createdAt));
        // A malformed document (missing required fields) that cannot be converted to the record.
        mongoTemplate.getCollection(COLLECTION_NAME).insertOne(
                new Document(PendingSharingInvitation.PROJECT_ID, projectId.id())
                        .append(PendingSharingInvitation.PERSON_KEY, "corrupt@x.org"));

        // The strict page-load read fails rather than silently dropping the corrupt document.
        assertThrows(RuntimeException.class, () -> repository.findByProjectId(projectId));

        // The tolerant login-time read skips the corrupt document and still returns the good one.
        List<PendingSharingInvitation> found = repository.findByPersonKeys(Set.of("good@x.org", "corrupt@x.org"));
        assertThat(found, hasSize(1));
        assertThat(found.get(0).personKey(), is("good@x.org"));
    }

    @Test
    public void shouldEnforceTheUniqueIndexOnProjectIdAndPersonKey() {
        // There is no production insert() path, so provoke the duplicate-key case with raw collection
        // writes to prove the unique (projectId, personKey) index is actually in place.
        var collection = mongoTemplate.getCollection(COLLECTION_NAME);
        collection.insertOne(new Document(PendingSharingInvitation.PROJECT_ID, projectId.id())
                                     .append(PendingSharingInvitation.PERSON_KEY, "new@x.org"));
        try {
            collection.insertOne(new Document(PendingSharingInvitation.PROJECT_ID, projectId.id())
                                         .append(PendingSharingInvitation.PERSON_KEY, "new@x.org"));
            fail("Expected a duplicate-key write error");
        } catch (MongoWriteException e) {
            assertThat(e.getError().getCategory(), is(ErrorCategory.DUPLICATE_KEY));
        }
    }
}
