package edu.stanford.bmir.protege.web.server.issues;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;


import edu.stanford.bmir.protege.web.server.dispatch.Result;
import edu.stanford.bmir.protege.web.server.event.EventList;
import edu.stanford.bmir.protege.web.server.event.HasEventList;
import edu.stanford.bmir.protege.web.server.event.ProjectEvent;
import edu.stanford.bmir.protege.web.server.project.HasProjectId;
import edu.stanford.bmir.protege.web.server.project.ProjectId;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Oct 2016
 */
@AutoValue

@JsonTypeName("AddEntityComment")
public abstract class AddEntityCommentResult implements Result, HasProjectId, HasEventList<ProjectEvent<?>> {

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    public abstract ThreadId getThreadId();

    public abstract Comment getComment();

    public abstract String getCommentRendering();

    @Override
    public abstract EventList<ProjectEvent<?>> getEventList();

    @JsonCreator
    public static AddEntityCommentResult create(@JsonProperty("projectId") ProjectId newProjectId,
                                                @JsonProperty("threadId") ThreadId newThreadId,
                                                @JsonProperty("comment") Comment newComment,
                                                @JsonProperty("commentRendering") String newCommentRendering,
                                                @JsonProperty("eventList") EventList<ProjectEvent<?>> newEventList) {
        return new AutoValue_AddEntityCommentResult(newProjectId,
                                                    newThreadId,
                                                    newComment,
                                                    newCommentRendering,
                                                    newEventList);
    }
}
