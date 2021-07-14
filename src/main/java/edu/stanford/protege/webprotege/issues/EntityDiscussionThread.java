package edu.stanford.protege.webprotege.issues;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.project.ProjectId;
import edu.stanford.protege.webprotege.user.UserId;
import org.semanticweb.owlapi.model.OWLEntity;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.annotation.Nonnull;
import java.util.List;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Oct 2016
 *
 * A thread of comments that are attached to an entity
 */
@Document(collection = "EntityDiscussionThreads")
public class EntityDiscussionThread {

    public static final String PROJECT_ID = "projectId";

    public static final String STATUS = "status";

    public static final String COMMENTS = "comments";

    public static final String COMMENTS_ID = "comments._id";

    public static final String ENTITY = "entity";

    @Id
    private ThreadId id;

    private ProjectId projectId;

    private OWLEntity entity;

    private Status status;

    private List<Comment> comments;

    @JsonCreator
    public EntityDiscussionThread(@JsonProperty("id") @Nonnull ThreadId id,
                                  @JsonProperty("projectId") @Nonnull ProjectId projectId,
                                  @JsonProperty("entity") @Nonnull OWLEntity entity,
                                  @JsonProperty("status") @Nonnull Status status,
                                  @JsonProperty("comments") @Nonnull ImmutableList<Comment> comments) {
        this.id = checkNotNull(id);
        this.projectId = checkNotNull(projectId);
        this.entity = checkNotNull(entity);
        this.comments = checkNotNull(comments);
        this.status = checkNotNull(status);
    }


    private EntityDiscussionThread() {
    }

    public ProjectId getProjectId() {
        return projectId;
    }

    public ThreadId getId() {
        return id;
    }

    @Nonnull
    public OWLEntity getEntity() {
        return entity;
    }

    @Nonnull
    public Status getStatus() {
        return status;
    }

    @Nonnull
    public ImmutableList<Comment> getComments() {
        return ImmutableList.copyOf(comments);
    }

    /**
     * Determines whether or not the thread was created by the specified user.  The user who posted the first
     * comment is deemed to have created a thread.
     * @param userId The userId to test for.
     * @return {@code true} if the thread was created by the user, otherwise {@code false}.
     */
    public boolean isCreatedBy(UserId userId) {
        return !comments.isEmpty() && comments.get(0).getCreatedBy().equals(userId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, entity, comments, status);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof EntityDiscussionThread)) {
            return false;
        }
        EntityDiscussionThread other = (EntityDiscussionThread) obj;
        return this.id.equals(other.id)
                && this.entity.equals(other.entity)
                && this.comments.equals(other.comments)
                && this.status.equals(other.status);
    }


    @Override
    public String toString() {
        return toStringHelper("EntityCommentsThread")
                .addValue(id)
                .add("entity", entity)
                .add("status", status)
                .add("comments", comments)
                .toString();
    }
}
