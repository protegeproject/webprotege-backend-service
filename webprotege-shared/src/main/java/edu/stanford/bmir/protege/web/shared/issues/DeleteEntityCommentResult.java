package edu.stanford.bmir.protege.web.shared.issues;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 9 Oct 2016
 */
@AutoValue
@GwtCompatible(serializable = true)
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
