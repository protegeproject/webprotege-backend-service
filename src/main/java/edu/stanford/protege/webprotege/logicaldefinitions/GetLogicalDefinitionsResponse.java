package edu.stanford.protege.webprotege.logicaldefinitions;

import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.frame.PropertyClassValue;

import java.util.List;

@JsonTypeName(GetLogicalDefinitionsAction.CHANNEL)
public record GetLogicalDefinitionsResponse(List<LogicalDefinition> logicalDefinitions,
                                            List<PropertyClassValue> necessaryConditions) implements Result {
}
