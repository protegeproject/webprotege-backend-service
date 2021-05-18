package edu.stanford.bmir.protege.web.server.webhook;

import edu.stanford.bmir.protege.web.server.inject.ApplicationSingleton;
import edu.stanford.bmir.protege.web.server.project.ProjectId;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 May 2017
 */
@ApplicationSingleton
public interface WebhookRepository {

    void clearProjectWebhooks(ProjectId projectId);

    void addProjectWebhooks(List<ProjectWebhook> projectWebhooks);

    List<ProjectWebhook> getProjectWebhooks(@Nonnull ProjectId projectId);

    List<ProjectWebhook> getProjectWebhooks(@Nonnull ProjectId projectId, ProjectWebhookEventType event);
}
