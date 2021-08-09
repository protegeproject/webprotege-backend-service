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
 * Date: 20/02/2013
 */
@AutoValue

@JsonTypeName("UpdateNamedIndividualFrame")
public abstract class UpdateNamedIndividualFrameAction extends UpdateFrameAction {


    @JsonCreator
    public static UpdateNamedIndividualFrameAction create(@JsonProperty("projectId") ProjectId projectId,
                                                       @JsonProperty("from") PlainNamedIndividualFrame from,
                                                       @JsonProperty("to") PlainNamedIndividualFrame to) {
        return new AutoValue_UpdateNamedIndividualFrameAction(projectId, from, to);
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    @Override
    public abstract PlainNamedIndividualFrame getFrom();

    @Override
    public abstract PlainNamedIndividualFrame getTo();
}
