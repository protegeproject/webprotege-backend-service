package edu.stanford.protege.webprotege.frame;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;

import javax.annotation.Nonnull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
@AutoValue

@JsonTypeName("UpdateAnnotationPropertyFrame")
public abstract class UpdateAnnotationPropertyFrameAction implements UpdateFrame, ProjectAction<UpdateAnnotationPropertyFrameResult> {

    public static final String CHANNEL = "webprotege.frames.UpdateAnnotationPropertyFrame";

    @JsonCreator
    public static UpdateAnnotationPropertyFrameAction create(@JsonProperty("projectId") ProjectId projectId,
                                                             @JsonProperty("from") PlainAnnotationPropertyFrame from,
                                                             @JsonProperty("to") PlainAnnotationPropertyFrame to) {
        return new AutoValue_UpdateAnnotationPropertyFrameAction(projectId, from, to);
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    @Override
    public abstract PlainAnnotationPropertyFrame getFrom();

    @Override
    public abstract PlainAnnotationPropertyFrame getTo();
}
