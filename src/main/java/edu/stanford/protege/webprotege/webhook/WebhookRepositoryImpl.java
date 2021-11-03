package edu.stanford.protege.webprotege.webhook;

import edu.stanford.protege.webprotege.common.ProjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.protege.webprotege.webhook.ProjectWebhook.PROJECT_ID;
import static edu.stanford.protege.webprotege.webhook.ProjectWebhook.SUBSCRIBED_TO_EVENTS;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 May 2017
 */
public class WebhookRepositoryImpl implements WebhookRepository {

    private final MongoTemplate mongoTemplate;

    @Inject
    public WebhookRepositoryImpl(@Nonnull MongoTemplate mongoTemplate) {
        this.mongoTemplate = checkNotNull(mongoTemplate);
    }

    @Override
    public void clearProjectWebhooks(ProjectId projectId) {
        mongoTemplate.remove(queryByProjectId(projectId), ProjectWebhook.class);
    }

    private Query queryByProjectId(ProjectId projectId) {
        return query(where(PROJECT_ID).is(projectId.value()));
    }

    @Override
    public void addProjectWebhooks(List<ProjectWebhook> projectWebhooks) {
        mongoTemplate.insertAll(projectWebhooks);
    }

    @Override
    public List<ProjectWebhook> getProjectWebhooks(@Nonnull ProjectId projectId) {
        return mongoTemplate.find(queryByProjectId(projectId), ProjectWebhook.class);
    }

    @Override
    public List<ProjectWebhook> getProjectWebhooks(@Nonnull ProjectId projectId, ProjectWebhookEventType event) {
        var query = queryByProjectId(projectId)
                .addCriteria(where(SUBSCRIBED_TO_EVENTS).is(event));
        return mongoTemplate.find(query, ProjectWebhook.class);
    }
}
