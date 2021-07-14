package edu.stanford.protege.webprotege.issues;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.Result;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 9 Oct 2016
 */
@AutoValue

@JsonTypeName("DeleteEntityComment")
public abstract class DeleteEntityCommentResult implements Result {

    @JsonCreator
    public static DeleteEntityCommentResult create(@JsonProperty("commentId") @Nonnull CommentId commentId,
                                                   @JsonProperty("deleted") boolean deleted) {
        return new AutoValue_DeleteEntityCommentResult(commentId, deleted);
    }

    @Nonnull
    public abstract CommentId getCommentId();

    @JsonProperty("deleted")
    public abstract boolean wasDeleted();
}
