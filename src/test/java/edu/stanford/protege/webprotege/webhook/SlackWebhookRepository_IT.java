package edu.stanford.protege.webprotege.webhook;

import com.mongodb.client.MongoClient;
import edu.stanford.protege.webprotege.persistence.MongoTestUtils;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 8 Jun 2017
 */
public class SlackWebhookRepository_IT {

    private static final String PAYLOAD_URL_A = "payloadurlA";

    private static final String PAYLOAD_URL_B = "payloadurlB";

    @Autowired
    private SlackWebhookRepositoryImpl repository;

    private MongoClient mongoClient;

    private MongoTemplate mongoTemplate;

    private SlackWebhook slackWebhookA, slackWebhookB;

    private ProjectId projectId = ProjectId.valueOf("12345678-1234-1234-1234-123456789abc");

    @Before
    public void setUp() {
        mongoClient = MongoTestUtils.createMongoClient();
        mongoTemplate = new MongoTemplate(mongoClient, MongoTestUtils.getTestDbName());
        repository = new SlackWebhookRepositoryImpl(mongoTemplate);
        repository.ensureIndexes();
        slackWebhookA = new SlackWebhook(projectId, PAYLOAD_URL_A);
        slackWebhookB = new SlackWebhook(projectId, PAYLOAD_URL_B);
    }

    @Test
    public void shouldSaveWebhook() {
        repository.addWebhooks(singletonList(slackWebhookA));
        assertThat(countDocuments(), is(1L));
    }

    private long countDocuments() {
        return mongoTemplate.getCollection("SlackWebhooks").countDocuments();
    }

    @Test
    public void shouldSaveMultipleWebhooks() {
        repository.addWebhooks(asList(slackWebhookA, slackWebhookB));
        assertThat(countDocuments(), is(2L));
    }

    @Test
    public void shouldNotSaveDuplicates() {
        repository.addWebhooks(Collections.singletonList(slackWebhookA));
        repository.addWebhooks(Collections.singletonList(slackWebhookA));
        assertThat(countDocuments(), is(1L));
    }

    @Test
    public void shouldFindWebhookByProjectId() {
        repository.addWebhooks(Collections.singletonList(slackWebhookA));
        List<SlackWebhook> webhooks = repository.getWebhooks(projectId);
        assertThat(webhooks, is(singletonList(slackWebhookA)));
    }

    @Test
    public void shouldClearWebhook() {
        repository.addWebhooks(Collections.singletonList(slackWebhookA));
        repository.clearWebhooks(projectId);
        assertThat(countDocuments(), is(0L));
    }

    @After
    public void tearDown() throws Exception {
        mongoTemplate.getDb().drop();
        mongoClient.close();
    }
}
