package edu.stanford.protege.webprotege.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import org.semanticweb.owlapi.model.IRI;

@JsonTypeName(GetIcatxEntityTypeAction.CHANNEL)
public record GetIcatxEntityTypeAction(@JsonProperty("projectId") ProjectId projectId, @JsonProperty("entityIri") IRI entityIri)  implements ProjectAction<GetIcatxEntityTypeResult> {

    public static final String CHANNEL = "webprotege.entities.GetIcatxEntityType";

    @Override
    public ProjectId projectId() {
        return projectId;
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }
}
