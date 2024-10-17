package edu.stanford.protege.webprotege.logicaldefinitions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.ChangeRequestId;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import org.semanticweb.owlapi.model.OWLClass;

@JsonTypeName(UpdateLogicalDefinitionsAction.CHANNEL)
public record UpdateLogicalDefinitionsAction(
        @JsonProperty("changeRequestId") ChangeRequestId changeRequestId,
        @JsonProperty("projectId") ProjectId projectId,
        @JsonProperty("subject") OWLClass subject,
        @JsonProperty("pristineLogicalConditions") LogicalConditions pristineLogicalConditions,
        @JsonProperty("changedLogicalConditions") LogicalConditions changedLogicalConditions,
        @JsonProperty("commitMessage") String commitMessage
        ) implements ProjectAction<UpdateLogicalDefinitionsResponse> {


    public final static String CHANNEL = "icatx.logicalDefinitions.UpdateLogicalDefinitions";

    @Override
    public String getChannel() {
        return CHANNEL;
    }

    public static UpdateLogicalDefinitionsAction create(ChangeRequestId changeRequestId,
                                                        ProjectId projectId,
                                                        OWLClass subject,
                                                        LogicalConditions pristineLogicalConditions,
                                                        LogicalConditions changedLogicalConditions,
                                                        String commitMessage) {
        return new UpdateLogicalDefinitionsAction(changeRequestId, projectId, subject,
                pristineLogicalConditions, changedLogicalConditions, commitMessage);
    }
}
