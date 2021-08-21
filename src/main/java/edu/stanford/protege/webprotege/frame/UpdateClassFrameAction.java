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
 * Date: 20/02/2013
 */
@AutoValue

@JsonTypeName("UpdateClassFrame")
public abstract class UpdateClassFrameAction implements UpdateFrameAction, ProjectAction<UpdateClassFrameResult> {

    public static final String CHANNEL = "frames.UpdateClassFrame";

    @JsonCreator
    public static UpdateClassFrameAction create(@JsonProperty("projectId") ProjectId projectId,
                                                @JsonProperty("from") PlainClassFrame from,
                                                @JsonProperty("to") PlainClassFrame to) {
        return new AutoValue_UpdateClassFrameAction(projectId, from, to);
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    @Override
    public abstract PlainClassFrame getFrom();

    @Override
    public abstract PlainClassFrame getTo();
}
