package edu.stanford.protege.webprotege.issues;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.project.HasProjectId;
import edu.stanford.protege.webprotege.common.ProjectId;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Oct 2016
 */
@AutoValue

@JsonTypeName("AddEntityComment")
public abstract class AddEntityCommentAction implements ProjectAction<AddEntityCommentResult>, HasProjectId {

    @JsonCreator
    public static AddEntityCommentAction addComment(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                                    @JsonProperty("threadId") @Nonnull ThreadId threadId,
                                                    @JsonProperty("comment") @Nonnull String comment) {
        return new AutoValue_AddEntityCommentAction(projectId, threadId, comment);
    }

    @Nonnull
    @JsonProperty("projectId")
    public abstract ProjectId getProjectId();

    @JsonProperty("threadId")
    public abstract ThreadId getThreadId();

    @JsonProperty("comment")
    public abstract String getComment();
}
