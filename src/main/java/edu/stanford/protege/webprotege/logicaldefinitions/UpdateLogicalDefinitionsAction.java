package edu.stanford.protege.webprotege.logicaldefinitions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import org.semanticweb.owlapi.model.OWLClass;

@JsonTypeName(UpdateLogicalDefinitionsAction.CHANNEL)
public record UpdateLogicalDefinitionsAction(
        @JsonProperty("projectId") ProjectId projectId,
        @JsonProperty("subject") OWLClass subject,
        @JsonProperty("pristineLogicalConditions") LogicalConditions pristineLogicalConditions,
        @JsonProperty("changedLogicalConditions") LogicalConditions changedLogicalConditions
        ) implements ProjectAction<UpdateLogicalDefinitionsResponse> {


    public final static String CHANNEL = "icatx.logicalDefinitions.UpdateLogicalDefinitions";

    @Override
    public String getChannel() {
        return CHANNEL;
    }

    public static UpdateLogicalDefinitionsAction create(ProjectId projectId, OWLClass subject,
                                                        LogicalConditions pristineLogicalConditions,
                                                        LogicalConditions changedLogicalConditions) {
        return new UpdateLogicalDefinitionsAction(projectId, subject, pristineLogicalConditions, changedLogicalConditions);
    }
}
