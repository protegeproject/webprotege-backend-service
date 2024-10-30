package edu.stanford.protege.webprotege.logicaldefinitions;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.stanford.protege.webprotege.frame.PropertyClassValue;

import java.util.List;

public record LogicalConditions (
    @JsonProperty("logicalDefinitions") List<LogicalDefinition> logicalDefinitions,
    @JsonProperty("necessaryConditions") List<PropertyClassValue> necessaryConditions
    ){
}
