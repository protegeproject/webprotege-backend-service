package edu.stanford.protege.webprotege.logicaldefinitions;

import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.Response;
import edu.stanford.protege.webprotege.frame.PlainPropertyClassValue;

import java.util.List;

@JsonTypeName(GetLogicalDefinitionsRequest.CHANNEL)
public record GetLogicalDefinitionsResponse(List<LogicalDefinition> logicalDefinitions,
                                            List<PlainPropertyClassValue> necessaryConditions) implements Response  {
}
