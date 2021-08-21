package edu.stanford.protege.webprotege.frame;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.project.HasProjectId;
import edu.stanford.protege.webprotege.common.ProjectId;

import javax.annotation.Nonnull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
@AutoValue

@JsonTypeName("UpdateDataPropertyFrame")
public abstract class UpdateDataPropertyFrameAction implements UpdateFrameAction, ProjectAction<UpdateDataPropertyFrameResult> {

    public static final String CHANNEL = "frames.UpdateDataPropertyFrame";

    @JsonCreator
    public static UpdateDataPropertyFrameAction create(@JsonProperty("projectId") ProjectId projectId,
                                                @JsonProperty("from") PlainDataPropertyFrame from,
                                                @JsonProperty("to") PlainDataPropertyFrame to) {
        return new AutoValue_UpdateDataPropertyFrameAction(projectId, from, to);
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    @Override
    public abstract PlainDataPropertyFrame getFrom();

    @Override
    public abstract PlainDataPropertyFrame getTo();
}
