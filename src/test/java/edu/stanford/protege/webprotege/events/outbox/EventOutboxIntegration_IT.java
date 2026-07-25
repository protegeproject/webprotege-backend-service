package edu.stanford.protege.webprotege.events.outbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.MongoTestExtension;
import edu.stanford.protege.webprotege.RabbitTestExtension;
import edu.stanford.protege.webprotege.WebprotegeBackendMonolithApplication;
import edu.stanford.protege.webprotege.common.EventId;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.event.BrowserTextChangedEvent;
import edu.stanford.protege.webprotege.ipc.Headers;
import edu.stanford.protege.webprotege.ipc.impl.RabbitMQEventsConfiguration;
import edu.stanford.protege.webprotege.project.PackagedProjectChangeEvent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.OptionalLong;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * End-to-end tests for the outbox: a change event enqueued through {@link EventOutbox} is republished by the
 * {@link EventOutboxRelay} to the real event exchange and marked sent, and a durability-gated event is held
 * back until its project's watermark advances.
 */
@SpringBootTest
@Import({WebprotegeBackendMonolithApplication.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@ExtendWith({SpringExtension.class, MongoTestExtension.class, RabbitTestExtension.class})
@ActiveProfiles("test")
public class EventOutboxIntegration_IT {

    @Autowired
    private EventOutbox eventOutbox;

    @Autowired
    private EventOutboxRepository repository;

    @Autowired
    private EventOutboxRelay relay;

    @Autowired
    private DurableRevisionWatermarkRegistry watermarkRegistry;

    @Autowired
    private OWLDataFactory dataFactory;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ConnectionFactory connectionFactory;

    private RabbitTemplate rabbitTemplate;

    private String queueName;

    private ProjectId projectId;

    @BeforeEach
    void setUp(@Autowired MongoTemplate mongoTemplate) {
        mongoTemplate.dropCollection(EventOutboxRecord.COLLECTION);
        projectId = ProjectId.valueOf(UUID.randomUUID().toString());
        rabbitTemplate = new RabbitTemplate(connectionFactory);

        // Bind a private test queue to the fanout event exchange so we can observe what the relay publishes.
        queueName = "test-outbox-" + UUID.randomUUID();
        var admin = new RabbitAdmin(connectionFactory);
        var exchange = new FanoutExchange(RabbitMQEventsConfiguration.EVENT_EXCHANGE, true, false);
        admin.declareExchange(exchange);
        var queue = new Queue(queueName, false, false, false);
        admin.declareQueue(queue);
        admin.declareBinding(BindingBuilder.bind(queue).to(exchange));
    }

    @AfterEach
    void tearDown() {
        watermarkRegistry.deregister(projectId);
        new RabbitAdmin(connectionFactory).deleteQueue(queueName);
    }

    @Test
    void shouldPublishPendingEventAndMarkItSent() throws Exception {
        eventOutbox.enqueue(changeEvent(), OptionalLong.empty());

        relay.drainOutbox();

        var message = rabbitTemplate.receive(queueName, 5000);
        assertNotNull(message, "the relay should have published the event to the exchange");
        var headers = message.getMessageProperties().getHeaders();
        assertEquals(projectId.value(), headers.get(Headers.PROJECT_ID));
        assertEquals(PackagedProjectChangeEvent.CHANNEL, headers.get(Headers.CHANNEL));
        var body = objectMapper.readTree(message.getBody());
        assertTrue(body.get("projectEvents").isArray(), "the published body should carry the change events");
        assertEquals(1, body.get("projectEvents").size());
        assertTrue(repository.findUnsentRecordsForProjectInOrder(projectId.value()).isEmpty(),
                   "the record should have been marked sent");
    }

    @Test
    void shouldNotPublishGatedEventUntilTheWatermarkReachesItsRevision() throws Exception {
        var watermark = new DurableRevisionWatermark(projectId, 0);
        watermarkRegistry.register(watermark);
        eventOutbox.enqueue(changeEvent(), OptionalLong.of(1));

        // Watermark is behind the event's revision: nothing is published and the row stays gated.
        relay.drainOutbox();
        assertNull(rabbitTemplate.receive(queueName, 1000), "a gated event must not be published");
        assertEquals(EventOutboxState.GATED,
                     repository.findUnsentRecordsForProjectInOrder(projectId.value()).get(0).getState());

        // The revision becomes durable: the row is promoted and published.
        watermark.advance();
        relay.drainOutbox();
        assertNotNull(rabbitTemplate.receive(queueName, 5000), "the event should be published once durable");
        assertTrue(repository.findUnsentRecordsForProjectInOrder(projectId.value()).isEmpty(),
                   "the record should have been marked sent");
    }

    private PackagedProjectChangeEvent changeEvent() {
        var entity = dataFactory.getOWLClass(IRI.create("http://example.org/A"));
        var nested = new BrowserTextChangedEvent(EventId.generate(), projectId, entity, "A", ImmutableList.of());
        return new PackagedProjectChangeEvent(projectId, EventId.generate(), List.of(nested));
    }
}
