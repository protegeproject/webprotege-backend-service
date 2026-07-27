package edu.stanford.protege.webprotege.events.outbox;

import edu.stanford.protege.webprotege.MongoTestExtension;
import edu.stanford.protege.webprotege.WebprotegeBackendMonolithApplication;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.annotation.DirtiesContext;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest(classes = WebprotegeBackendMonolithApplication.class, properties = "webprotege.rabbitmq.commands-subscribe=false")
@ExtendWith({MongoTestExtension.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class EventOutboxRepositoryImpl_IT {

    @Autowired
    private EventOutboxRepository repository;

    @Autowired
    private MongoTemplate mongoTemplate;

    private final Instant baseTime = Instant.parse("2026-07-24T00:00:00Z");

    @AfterEach
    void tearDown() {
        mongoTemplate.dropCollection(EventOutboxRecord.COLLECTION);
    }

    @Test
    void shouldReturnUnsentRecordsForProjectInInsertionOrder() {
        var projectId = freshProjectId();
        insert(projectId, 1, EventOutboxState.GATED, "first", baseTime);
        insert(projectId, 2, EventOutboxState.GATED, "second", baseTime.plusSeconds(1));
        insert(projectId, 3, EventOutboxState.PENDING, "third", baseTime.plusSeconds(2));

        var payloadsInOrder = repository.findUnsentRecordsForProjectInOrder(projectId.value())
                .stream().map(EventOutboxRecord::getPayload).collect(toList());

        assertThat(payloadsInOrder, contains("first", "second", "third"));
    }

    @Test
    void shouldExcludeSentRecordsFromUnsentQueries() {
        var projectId = freshProjectId();
        insert(projectId, 1, EventOutboxState.PENDING, "payload", baseTime);
        var record = repository.findUnsentRecordsForProjectInOrder(projectId.value()).get(0);

        repository.markSent(record.getId());

        assertThat(repository.findUnsentRecordsForProjectInOrder(projectId.value()), is(empty()));
        assertThat(repository.findProjectIdsWithUnsentRecords(), not(hasItem(projectId.value())));
    }

    @Test
    void shouldPromoteOnlyGatedRecordsUpToTheWatermark() {
        var projectId = freshProjectId();
        insert(projectId, 1, EventOutboxState.GATED, "rev1", baseTime);
        insert(projectId, 2, EventOutboxState.GATED, "rev2", baseTime.plusSeconds(1));
        insert(projectId, 3, EventOutboxState.GATED, "rev3", baseTime.plusSeconds(2));

        var promoted = repository.promoteGatedRecordsUpToRevision(projectId.value(), 2);

        assertThat(promoted, is(2L));
        var states = repository.findUnsentRecordsForProjectInOrder(projectId.value())
                .stream().map(EventOutboxRecord::getState).collect(toList());
        assertThat(states, contains(EventOutboxState.PENDING, EventOutboxState.PENDING, EventOutboxState.GATED));
    }

    @Test
    void shouldRecordFailedAttempt() {
        var projectId = freshProjectId();
        insert(projectId, 1, EventOutboxState.PENDING, "payload", baseTime);
        var record = repository.findUnsentRecordsForProjectInOrder(projectId.value()).get(0);
        var attemptedAt = baseTime.plusSeconds(5);

        repository.recordFailedAttempt(record.getId(), attemptedAt);

        var updated = repository.findUnsentRecordsForProjectInOrder(projectId.value()).get(0);
        assertThat(updated.getAttempts(), is(1));
        assertThat(updated.getLastAttemptAt(), is(attemptedAt));
    }

    @Test
    void shouldOnlyReportProjectsWithUnsentRecords() {
        var withUnsent = freshProjectId();
        var allSent = freshProjectId();
        insert(withUnsent, 1, EventOutboxState.PENDING, "pending", baseTime);
        insert(allSent, 1, EventOutboxState.PENDING, "willBeSent", baseTime);
        var sentRecord = repository.findUnsentRecordsForProjectInOrder(allSent.value()).get(0);
        repository.markSent(sentRecord.getId());

        var projectIds = repository.findProjectIdsWithUnsentRecords();

        assertThat(projectIds, hasItem(withUnsent.value()));
        assertThat(projectIds, not(hasItem(allSent.value())));
    }

    @Test
    void shouldReportTheOldestUnsentRecordCreationTime() {
        var projectId = freshProjectId();
        insert(projectId, 1, EventOutboxState.PENDING, "older", baseTime);
        insert(projectId, 2, EventOutboxState.GATED, "newer", baseTime.plusSeconds(30));

        var oldest = repository.findOldestUnsentRecordCreationTime();

        assertThat(oldest.isPresent(), is(true));
        assertThat(oldest.get(), is(baseTime));
    }

    private void insert(ProjectId projectId, long revision, EventOutboxState state, String payload, Instant createdAt) {
        repository.insert(EventOutboxRecord.create(projectId.value(), revision, "channel", payload, state, createdAt));
    }

    private ProjectId freshProjectId() {
        return ProjectId.valueOf(UUID.randomUUID().toString());
    }
}
