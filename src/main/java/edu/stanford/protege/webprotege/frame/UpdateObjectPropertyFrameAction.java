package edu.stanford.protege.webprotege.frame;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.common.ProjectId;

import javax.annotation.Nonnull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
@AutoValue

@JsonTypeName("UpdateObjectPropertyFrame")
public abstract class UpdateObjectPropertyFrameAction extends UpdateFrameAction {


    @JsonCreator
    public static UpdateObjectPropertyFrameAction create(@JsonProperty("projectId") ProjectId projectId,
                                                       @JsonProperty("from") PlainObjectPropertyFrame from,
                                                       @JsonProperty("to") PlainObjectPropertyFrame to) {
        return new AutoValue_UpdateObjectPropertyFrameAction(projectId, from, to);
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    @Override
    public abstract PlainObjectPropertyFrame getFrom();

    @Override
    public abstract PlainObjectPropertyFrame getTo();
}
