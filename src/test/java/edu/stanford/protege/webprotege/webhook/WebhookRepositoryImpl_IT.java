package edu.stanford.protege.webprotege.webhook;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static edu.stanford.protege.webprotege.webhook.ProjectWebhookEventType.COMMENT_POSTED;
import static edu.stanford.protege.webprotege.webhook.ProjectWebhookEventType.PROJECT_CHANGED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 May 2017
 */
@SpringBootTest
public class WebhookRepositoryImpl_IT {

    private static final String PAYLOAD_URL = "http://the.payload.url/path";

    @Autowired
    private WebhookRepositoryImpl repository;

    private ProjectId projectId;

    private List<ProjectWebhookEventType> subscribedToEvents;

    private ProjectWebhook webhook;

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    public void setUp() throws Exception {
        projectId = ProjectId.valueOf(UUID.randomUUID().toString());
        subscribedToEvents = Collections.singletonList(PROJECT_CHANGED);
        webhook = new ProjectWebhook(projectId,
                                     PAYLOAD_URL,
                                     subscribedToEvents);

        repository.addProjectWebhooks(Collections.singletonList(webhook));
    }

    private MongoCollection getCollection() {
        return mongoTemplate.getCollection("ProjectWebhooks");
    }

    @Test
    public void shouldSaveWebhook() {
        assertThat(getCollection().countDocuments(), is(1L));
    }

    @Test
    public void shouldFindWebhookByProjectId() {
        List<ProjectWebhook> webhooks = repository.getProjectWebhooks(projectId);
        assertThat(webhooks, hasItems(webhook));
    }

    @Test
    public void shouldFindWebhooksByProjectIdAndEventType() {
        List<ProjectWebhook> webhooks = repository.getProjectWebhooks(projectId, PROJECT_CHANGED);
        assertThat(webhooks, hasItems(webhook));
    }

    @Test
    public void shouldNotFindWebhooksByProjectIdAndDifferentEventType() {
        List<ProjectWebhook> webhooks = repository.getProjectWebhooks(projectId, COMMENT_POSTED);
        assertThat(webhooks.isEmpty(), is(true));
    }

    @Test
    public void shouldClearProjectWebhooks() {
        repository.clearProjectWebhooks(projectId);
        assertThat(getCollection().countDocuments(), is(0L));
    }

    @AfterEach
    void tearDown() {
        getCollection().drop();
    }
}
