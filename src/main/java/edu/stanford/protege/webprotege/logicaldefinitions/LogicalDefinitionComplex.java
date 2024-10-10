package edu.stanford.protege.webprotege.logicaldefinitions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.entity.OWLClassData;
import edu.stanford.protege.webprotege.frame.PropertyClassValue;


import java.util.List;

@JsonTypeName("LogicalDefinition")
public record LogicalDefinitionComplex(@JsonProperty("logicalDefinitionParent") OWLClassData logicalDefinitionParent,
                                       @JsonProperty("logicalDefinitions") List<PropertyClassValue> logicalDefinitions,
                                       @JsonProperty("necessaryConditions") List<PropertyClassValue> necessaryConditions) {
}
