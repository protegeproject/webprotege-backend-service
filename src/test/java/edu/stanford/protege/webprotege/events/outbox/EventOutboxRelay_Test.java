package edu.stanford.protege.webprotege.events.outbox;

import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.ipc.impl.RabbitMQEventsConfiguration;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the relay's ordering, head-of-line blocking, durability gate and back-off behaviour.  The
 * events template is a mock so that publish failures can be simulated deterministically.
 */
public class EventOutboxRelay_Test {

    private static final String EXCHANGE = RabbitMQEventsConfiguration.EVENT_EXCHANGE;

    private EventOutboxRepository repository;

    private RabbitTemplate eventRabbitTemplate;

    private DurableRevisionWatermarkRegistry watermarkRegistry;

    private MutableClock clock;

    private EventOutboxRelay relay;

    private final ProjectId projectP = ProjectId.valueOf("11111111-1111-1111-1111-111111111111");

    private final ProjectId projectQ = ProjectId.valueOf("22222222-2222-2222-2222-222222222222");

    @BeforeEach
    void setUp() {
        repository = mock(EventOutboxRepository.class);
        eventRabbitTemplate = mock(RabbitTemplate.class);
        watermarkRegistry = new DurableRevisionWatermarkRegistry();
        clock = new MutableClock(Instant.parse("2026-07-24T00:00:00Z"));
        relay = new EventOutboxRelay(repository, eventRabbitTemplate, watermarkRegistry, "backend", clock,
                                     1000, 60000, 30000);
        when(repository.findOldestUnsentRecordCreationTime()).thenReturn(Optional.empty());
    }

    @Test
    void happyPath_publishesPendingRecordsInOrderAndMarksSent() {
        var r1 = pending(projectP, "p1");
        var r2 = pending(projectP, "p2");
        when(repository.findProjectIdsWithUnsentRecords()).thenReturn(List.of(projectP.value()));
        when(repository.findUnsentRecordsForProjectInOrder(projectP.value())).thenReturn(List.of(r1, r2));

        relay.drainOutbox();

        var inOrder = inOrder(eventRabbitTemplate, repository);
        inOrder.verify(eventRabbitTemplate).send(eq(EXCHANGE), eq(""), argThat(bodyIs("p1")));
        inOrder.verify(repository).markSent(r1.getId());
        inOrder.verify(eventRabbitTemplate).send(eq(EXCHANGE), eq(""), argThat(bodyIs("p2")));
        inOrder.verify(repository).markSent(r2.getId());
        verify(repository, never()).recordFailedAttempt(any(), any());
    }

    @Test
    void brokerDown_leavesRecordPendingAndRecordsFailedAttempt() {
        var r1 = pending(projectP, "p1");
        when(repository.findProjectIdsWithUnsentRecords()).thenReturn(List.of(projectP.value()));
        when(repository.findUnsentRecordsForProjectInOrder(projectP.value())).thenReturn(List.of(r1));
        doThrow(new AmqpException("broker down")).when(eventRabbitTemplate).send(eq(EXCHANGE), eq(""), any(Message.class));

        relay.drainOutbox();

        verify(repository).recordFailedAttempt(r1.getId(), clock.instant());
        verify(repository, never()).markSent(any());
    }

    @Test
    void headOfLineFailure_blocksLaterRecordsForSameProjectButNotOtherProjects() {
        var r1 = pending(projectP, "p1");
        var r2 = pending(projectP, "p2");
        var q1 = pending(projectQ, "q1");
        when(repository.findProjectIdsWithUnsentRecords()).thenReturn(List.of(projectP.value(), projectQ.value()));
        when(repository.findUnsentRecordsForProjectInOrder(projectP.value())).thenReturn(List.of(r1, r2));
        when(repository.findUnsentRecordsForProjectInOrder(projectQ.value())).thenReturn(List.of(q1));
        doThrow(new AmqpException("broker down")).when(eventRabbitTemplate).send(eq(EXCHANGE), eq(""), argThat(bodyIs("p1")));

        relay.drainOutbox();

        // Project P blocks at r1; r2 is never published or marked sent.
        verify(repository).recordFailedAttempt(r1.getId(), clock.instant());
        verify(eventRabbitTemplate, never()).send(eq(EXCHANGE), eq(""), argThat(bodyIs("p2")));
        verify(repository, never()).markSent(r2.getId());
        // Project Q is unaffected.
        verify(eventRabbitTemplate).send(eq(EXCHANGE), eq(""), argThat(bodyIs("q1")));
        verify(repository).markSent(q1.getId());
    }

    @Test
    void gatedRecord_isNotPublishedAndBlocksLaterRecords() {
        var gated = gated(projectP, 5, "g1");
        var later = pending(projectP, "p2");
        when(repository.findProjectIdsWithUnsentRecords()).thenReturn(List.of(projectP.value()));
        when(repository.findUnsentRecordsForProjectInOrder(projectP.value())).thenReturn(List.of(gated, later));

        relay.drainOutbox();

        verify(eventRabbitTemplate, never()).send(anyString(), anyString(), any(Message.class));
        verify(repository, never()).markSent(any());
    }

    @Test
    void relay_promotesGatedRecordsUpToTheProjectWatermark() {
        watermarkRegistry.register(new DurableRevisionWatermark(projectP, 7));
        when(repository.findProjectIdsWithUnsentRecords()).thenReturn(List.of(projectP.value()));
        when(repository.findUnsentRecordsForProjectInOrder(projectP.value())).thenReturn(emptyList());

        relay.drainOutbox();

        verify(repository).promoteGatedRecordsUpToRevision(projectP.value(), 7);
    }

    @Test
    void relay_doesNotPromoteGatedRecordsForProjectsWithoutAWatermark() {
        when(repository.findProjectIdsWithUnsentRecords()).thenReturn(List.of(projectP.value()));
        when(repository.findUnsentRecordsForProjectInOrder(projectP.value())).thenReturn(emptyList());

        relay.drainOutbox();

        verify(repository, never()).promoteGatedRecordsUpToRevision(anyString(), anyLong());
    }

    @Test
    void backoff_skipsRecentlyFailedRecordUntilBackoffElapses() {
        var failed = new EventOutboxRecord(new ObjectId(), projectP.value(), 0, "channel", "p1",
                                           EventOutboxState.PENDING, 1, clock.instant(), clock.instant());
        when(repository.findProjectIdsWithUnsentRecords()).thenReturn(List.of(projectP.value()));
        when(repository.findUnsentRecordsForProjectInOrder(projectP.value())).thenReturn(List.of(failed));

        // Within the back-off window (base 1000 ms for the first retry): nothing published.
        clock.advanceMillis(500);
        relay.drainOutbox();
        verify(eventRabbitTemplate, never()).send(anyString(), anyString(), any(Message.class));

        // After the back-off window elapses: the record is published.
        clock.advanceMillis(1000);
        relay.drainOutbox();
        verify(eventRabbitTemplate).send(eq(EXCHANGE), eq(""), argThat(bodyIs("p1")));
        verify(repository).markSent(failed.getId());
    }

    private EventOutboxRecord pending(ProjectId projectId, String payload) {
        return new EventOutboxRecord(new ObjectId(), projectId.value(), 0, "channel", payload,
                                     EventOutboxState.PENDING, 0, null, clock.instant());
    }

    private EventOutboxRecord gated(ProjectId projectId, long revision, String payload) {
        return new EventOutboxRecord(new ObjectId(), projectId.value(), revision, "channel", payload,
                                     EventOutboxState.GATED, 0, null, clock.instant());
    }

    private static ArgumentMatcher<Message> bodyIs(String expected) {
        return message -> message != null && expected.equals(new String(message.getBody(), StandardCharsets.UTF_8));
    }

    private static final class MutableClock extends Clock {

        private Instant instant;

        private MutableClock(Instant instant) {
            this.instant = instant;
        }

        private void advanceMillis(long millis) {
            this.instant = this.instant.plusMillis(millis);
        }

        @Override
        public ZoneId getZone() {
            return ZoneOffset.UTC;
        }

        @Override
        public Clock withZone(ZoneId zone) {
            return this;
        }

        @Override
        public Instant instant() {
            return instant;
        }
    }
}
