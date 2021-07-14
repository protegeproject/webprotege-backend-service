package edu.stanford.protege.webprotege.issues;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.project.ProjectId;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 8 Oct 2016
 */
@AutoValue

@JsonTypeName("EditComment")
public abstract class EditCommentAction implements ProjectAction<EditCommentResult> {

    @JsonCreator
    public static EditCommentAction editComment(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                                @JsonProperty("threadId") @Nonnull ThreadId threadId,
                                                @JsonProperty("commentId") @Nonnull CommentId commentId,
                                                @JsonProperty("body") @Nonnull String body) {
        return new AutoValue_EditCommentAction(projectId, threadId, commentId, body);
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    public abstract ThreadId getThreadId();

    public abstract CommentId getCommentId();

    public abstract String getBody();
}
