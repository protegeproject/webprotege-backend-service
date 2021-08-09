package edu.stanford.protege.webprotege.webhook;

import edu.stanford.protege.webprotege.persistence.Repository;
import edu.stanford.protege.webprotege.common.ProjectId;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 8 Jun 2017
 */
public interface SlackWebhookRepository extends Repository {

    List<SlackWebhook> getWebhooks(@Nonnull ProjectId projectId);

    void clearWebhooks(@Nonnull ProjectId projectId);

    void addWebhooks(@Nonnull List<SlackWebhook> webhooks);
}
