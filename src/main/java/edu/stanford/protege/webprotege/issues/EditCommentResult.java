package edu.stanford.protege.webprotege.issues;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;


import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.event.EventList;
import edu.stanford.protege.webprotege.event.HasEventList;
import edu.stanford.protege.webprotege.event.ProjectEvent;

import javax.annotation.Nonnull;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 8 Oct 2016
 */
@AutoValue

@JsonTypeName("EditComment")
public abstract class EditCommentResult implements Result, HasEventList<ProjectEvent<?>> {

    @JsonCreator
    public static EditCommentResult create(@JsonProperty("editedComment") @Nonnull Optional<Comment> editedComment,
                             @JsonProperty("eventList") @Nonnull EventList<ProjectEvent<?>> projectEventList) {
        return new AutoValue_EditCommentResult(editedComment, projectEventList);
    }

    @Nonnull
    public abstract Optional<Comment> getEditedComment();

    @Override
    public abstract EventList<ProjectEvent<?>> getEventList();
}
