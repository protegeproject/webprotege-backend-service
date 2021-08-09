package edu.stanford.protege.webprotege.issues;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.Objects;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.common.ProjectId;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 9 Oct 2016
 */
@JsonTypeName("DeleteEntityComment")
public class DeleteEntityCommentAction implements ProjectAction<DeleteEntityCommentResult> {

    private ProjectId projectId;

    private CommentId commentId;

    @JsonCreator
    public static DeleteEntityCommentAction deleteComment(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                                          @JsonProperty("commentId") @Nonnull CommentId commentId) {
        return new DeleteEntityCommentAction(projectId, commentId);
    }

    public DeleteEntityCommentAction(@Nonnull ProjectId projectId,
                                     @Nonnull CommentId commentId) {
        this.commentId = checkNotNull(commentId);
        this.projectId = checkNotNull(projectId);
    }


    private DeleteEntityCommentAction() {
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    public CommentId getCommentId() {
        return commentId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(commentId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof DeleteEntityCommentAction)) {
            return false;
        }
        DeleteEntityCommentAction other = (DeleteEntityCommentAction) obj;
        return this.commentId.equals(other.commentId);
    }
}
