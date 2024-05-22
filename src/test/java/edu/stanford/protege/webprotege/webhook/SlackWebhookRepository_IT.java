package edu.stanford.protege.webprotege.webhook;

import com.mongodb.client.MongoCollection;
import edu.stanford.protege.webprotege.MongoTestExtension;
import edu.stanford.protege.webprotege.WebprotegeBackendMonolithApplication;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.bson.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.annotation.DirtiesContext;

import javax.annotation.Nonnull;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 8 Jun 2017
 */
@SpringBootTest(classes = WebprotegeBackendMonolithApplication.class)
@ExtendWith({MongoTestExtension.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class SlackWebhookRepository_IT {

    private static final String PAYLOAD_URL_A = "payloadurlA";

    private static final String PAYLOAD_URL_B = "payloadurlB";

    @Autowired
    private SlackWebhookRepositoryImpl repository;

    @Autowired
    private MongoTemplate mongoTemplate;

    private SlackWebhook slackWebhookA, slackWebhookB;

    private ProjectId projectId = ProjectId.valueOf("12345678-1234-1234-1234-123456789abc");

    @BeforeEach
    public void setUp() {
        repository.ensureIndexes();
        slackWebhookA = new SlackWebhook(projectId, PAYLOAD_URL_A);
        slackWebhookB = new SlackWebhook(projectId, PAYLOAD_URL_B);
    }

    @Test
    public void shouldSaveWebhook() {
        repository.addWebhook(slackWebhookA);
        assertThat(countDocuments(), is(1L));
    }

    private long countDocuments() {
        return getCollection().countDocuments();
    }

    @Nonnull
    private MongoCollection<Document> getCollection() {
        return mongoTemplate.getCollection("SlackWebhooks");
    }

    @Test
    public void shouldNotSaveDuplicates() {
        repository.addWebhook(slackWebhookA);
        repository.addWebhook(slackWebhookA);
        assertThat(countDocuments(), is(1L));
    }

    @Test
    public void shouldFindWebhookByProjectId() {
        repository.addWebhook(slackWebhookA);
        List<SlackWebhook> webhooks = repository.getWebhooks(projectId);
        assertThat(webhooks, is(singletonList(slackWebhookA)));
    }

    @Test
    public void shouldClearWebhook() {
        repository.addWebhook(slackWebhookA);
        repository.clearWebhooks(projectId);
        assertThat(countDocuments(), is(0L));
    }

    @AfterEach
    public void tearDown() throws Exception {
        getCollection().drop();
    }
}
