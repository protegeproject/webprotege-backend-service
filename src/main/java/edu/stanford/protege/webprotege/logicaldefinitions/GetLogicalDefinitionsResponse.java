package edu.stanford.protege.webprotege.logicaldefinitions;

import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.Response;

import java.util.List;

@JsonTypeName(GetLogicalDefinitionsRequest.CHANNEL)
public record GetLogicalDefinitionsResponse(List<LogicalDefinition> logicalDefinitions,
                                            List<NecessaryCondition> necessaryConditions) implements Response  {
}
