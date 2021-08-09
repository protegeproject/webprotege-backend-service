package edu.stanford.protege.webprotege.webhook;

import com.google.common.base.Objects;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 8 Jun 2017
 */
@Document(collection = "SlackWebhooks")
public class SlackWebhook implements Webhook {


    public static final String PROJECT_ID = "projectId";

    public static final String PAYLOAD_URL = "payloadUrl";

    private ProjectId projectId;

    private String payloadUrl;

    public SlackWebhook(@Nonnull ProjectId projectId,
                        @Nonnull String payloadUrl) {
        this.projectId = checkNotNull(projectId);
        this.payloadUrl = checkNotNull(payloadUrl);
    }

    @Nonnull
    public ProjectId getProjectId() {
        return projectId;
    }

    @Nonnull
    public String getPayloadUrl() {
        return payloadUrl;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectId, payloadUrl);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SlackWebhook)) {
            return false;
        }
        SlackWebhook other = (SlackWebhook) obj;
        return this.projectId.equals(other.projectId)
                && this.payloadUrl.equals(other.payloadUrl);
    }


    @Override
    public String toString() {
        return toStringHelper("SlackWebhook")
                .addValue(projectId)
                .add("payloadUrl", payloadUrl)
                .toString();
    }
}
