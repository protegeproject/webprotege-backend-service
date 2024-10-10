package edu.stanford.protege.webprotege.logicaldefinitions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.Request;
import edu.stanford.protege.webprotege.linearization.CreateLinearizationFromParentResponse;
import org.semanticweb.owlapi.model.OWLClass;

@JsonTypeName(GetLogicalDefinitionsRequest.CHANNEL)
public record GetLogicalDefinitionsRequest(
        @JsonProperty("projectId") ProjectId projectId,
        @JsonProperty("subject") OWLClass subject
        ) implements Request<GetLogicalDefinitionsResponse> {


    public final static String CHANNEL = "icatx.logicalDefinitions.LogicalDefinition";

    @Override
    public String getChannel() {
        return CHANNEL;
    }

    public static GetLogicalDefinitionsRequest create(ProjectId projectId, OWLClass subject) {
        return new GetLogicalDefinitionsRequest(projectId, subject);
    }
}
