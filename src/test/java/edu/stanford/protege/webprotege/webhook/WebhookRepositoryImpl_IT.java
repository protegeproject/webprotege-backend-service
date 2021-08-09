package edu.stanford.protege.webprotege.webhook;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import edu.stanford.protege.webprotege.persistence.MongoTestUtils;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
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
public class WebhookRepositoryImpl_IT {

    private static final String PAYLOAD_URL = "http://the.payload.url/path";

    private WebhookRepositoryImpl repository;

    private MongoClient client;

    private ProjectId projectId;

    private List<ProjectWebhookEventType> subscribedToEvents;

    private ProjectWebhook webhook;

    private MongoTemplate mongoTemplate;

    @Before
    public void setUp() throws Exception {
        client = MongoClients.create();
        mongoTemplate = new MongoTemplate(client, MongoTestUtils.getTestDbName());
        repository = new WebhookRepositoryImpl(mongoTemplate);
        projectId = ProjectId.get(UUID.randomUUID().toString());
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

    @After
    public void tearDown() throws Exception {
        mongoTemplate.getDb().drop();
        client.close();
    }
}
