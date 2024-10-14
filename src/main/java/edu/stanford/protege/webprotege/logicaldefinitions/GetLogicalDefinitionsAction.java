package edu.stanford.protege.webprotege.logicaldefinitions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import org.semanticweb.owlapi.model.OWLClass;

@JsonTypeName(GetLogicalDefinitionsAction.CHANNEL)
public record GetLogicalDefinitionsAction(
        @JsonProperty("projectId") ProjectId projectId,
        @JsonProperty("subject") OWLClass subject
        ) implements ProjectAction<GetLogicalDefinitionsResponse> {


    public final static String CHANNEL = "icatx.logicalDefinitions.GetLogicalDefinitions";

    @Override
    public String getChannel() {
        return CHANNEL;
    }

    public static GetLogicalDefinitionsAction create(ProjectId projectId, OWLClass subject) {
        return new GetLogicalDefinitionsAction(projectId, subject);
    }
}
