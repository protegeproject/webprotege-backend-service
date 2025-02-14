package edu.stanford.protege.webprotege.mail;

import edu.stanford.protege.webprotege.app.ApplicationHostSupplier;
import edu.stanford.protege.webprotege.common.ProjectId;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 4 Apr 2017
 */
public class MessageIdGenerator {

    private final ApplicationHostSupplier applicationHostSupplier;

    @Inject
    public MessageIdGenerator(@Nonnull ApplicationHostSupplier applicationHostSupplier) {
        this.applicationHostSupplier = checkNotNull(applicationHostSupplier);
    }

    /**
     * Generates a globally unique {@link MessageId}
     * @return The generated {@link MessageId}.
     */
    public MessageId generateUniqueMessageId() {
        String id = String.format("<%s@%s>", UUID.randomUUID(), applicationHostSupplier.get());
        return new MessageId(id);
    }

    /**
     * Generates a value for the message-id field of an email header based upon the id of some kind of project object,
     * for example a comment.
     * The generated id will contain the project id, the type of project object and the id for the project object (which
     * must be unique).
     * @param projectId The project id.
     * @param objectCategory The project object category, for example "comments" or "revisions"
     * @param objectId The id of the project object.
     * @return A string that can be used as the message-id field value in order to identify a message about an
     * object.
     */
    @Nonnull
    public MessageId generateProjectMessageId(@Nonnull ProjectId projectId,
                                           @Nonnull String objectCategory,
                                           @Nonnull String objectId) {
        String id = String.format("<projects/%s/%s/%s@%s>" ,
                                      checkNotNull(projectId).id(),
                                      checkNotNull(objectCategory),
                                      checkNotNull(objectId),
                                      applicationHostSupplier.get());
        return new MessageId(id);
    }
}
