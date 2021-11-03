package edu.stanford.protege.webprotege.webhook;

import com.mongodb.DuplicateKeyException;
import com.mongodb.bulk.BulkWriteUpsert;
import edu.stanford.protege.webprotege.inject.ApplicationSingleton;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicUpdate;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.UpdateDefinition;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.protege.webprotege.webhook.SlackWebhook.PROJECT_ID;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 8 Jun 2017
 */
@ApplicationSingleton
public class SlackWebhookRepositoryImpl implements SlackWebhookRepository {

    private static final Logger logger = LoggerFactory.getLogger(SlackWebhookRepositoryImpl.class);

    private final MongoTemplate mongo;

    @Inject
    public SlackWebhookRepositoryImpl(@Nonnull MongoTemplate mongo) {
        this.mongo = checkNotNull(mongo);
    }

    @Override
    public void ensureIndexes() {

    }

    @Override
    public List<SlackWebhook> getWebhooks(@Nonnull ProjectId projectId) {
        var query = query(where(PROJECT_ID).is(projectId));
        return mongo.find(query, SlackWebhook.class);
    }

    @Override
    public void clearWebhooks(@Nonnull ProjectId projectId) {
        var query = query(where(PROJECT_ID).is(projectId));
        mongo.remove(query, SlackWebhook.class);
    }

    @Override
    public void addWebhook(@Nonnull SlackWebhook webhook) {
        try {
            mongo.save(webhook);
        } catch (DuplicateKeyException e) {
            logger.debug("Ignored duplicate webhook", e);
        }
    }
}
