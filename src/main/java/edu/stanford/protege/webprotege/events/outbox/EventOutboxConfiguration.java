package edu.stanford.protege.webprotege.events.outbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.Clock;

/**
 * Wiring for the durable event outbox and its retrying relay.  {@link EnableScheduling} is switched on here
 * (the application does not otherwise enable scheduling) so that {@link EventOutboxRelay#drainOutbox()} runs
 * on a fixed delay.
 */
@Configuration
@EnableScheduling
public class EventOutboxConfiguration {

    @Bean
    @ConditionalOnMissingBean
    Clock outboxClock() {
        return Clock.systemUTC();
    }

    @Bean
    EventOutboxRepository eventOutboxRepository(MongoTemplate mongoTemplate) {
        return new EventOutboxRepositoryImpl(mongoTemplate);
    }

    @Bean
    DurableRevisionWatermarkRegistry durableRevisionWatermarkRegistry() {
        return new DurableRevisionWatermarkRegistry();
    }

    @Bean
    EventOutbox eventOutbox(EventOutboxRepository eventOutboxRepository,
                            ObjectMapper objectMapper,
                            Clock outboxClock) {
        return new EventOutbox(eventOutboxRepository, objectMapper, outboxClock);
    }

    @Bean
    EventOutboxRelay eventOutboxRelay(EventOutboxRepository eventOutboxRepository,
                                      @Qualifier("eventRabbitTemplate") RabbitTemplate eventRabbitTemplate,
                                      DurableRevisionWatermarkRegistry durableRevisionWatermarkRegistry,
                                      @Value("${spring.application.name}") String applicationName,
                                      Clock outboxClock,
                                      @Value("${webprotege.events.outbox.base-backoff-ms:1000}") long baseBackoffMillis,
                                      @Value("${webprotege.events.outbox.max-backoff-ms:60000}") long maxBackoffMillis,
                                      @Value("${webprotege.events.outbox.oldest-pending-warn-ms:30000}") long oldestPendingWarnThresholdMillis) {
        return new EventOutboxRelay(eventOutboxRepository,
                                    eventRabbitTemplate,
                                    durableRevisionWatermarkRegistry,
                                    applicationName,
                                    outboxClock,
                                    baseBackoffMillis,
                                    maxBackoffMillis,
                                    oldestPendingWarnThresholdMillis);
    }

    @Bean
    @ConditionalOnProperty(prefix = "webprotege.events.outbox", name = "relay-enabled", matchIfMissing = true)
    EventOutboxRelayScheduler eventOutboxRelayScheduler(EventOutboxRelay eventOutboxRelay) {
        return new EventOutboxRelayScheduler(eventOutboxRelay);
    }
}
