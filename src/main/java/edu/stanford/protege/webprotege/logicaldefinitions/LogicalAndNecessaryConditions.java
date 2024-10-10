package edu.stanford.protege.webprotege.logicaldefinitions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.semanticweb.owlapi.model.IRI;

import java.util.List;
import java.util.Map;

@JsonTypeName("LogicalAndNecessaryConditions")
public record LogicalAndNecessaryConditions(
        @JsonProperty("logicalDefinitions") List<LogicalDefinition> logicalDefinitions,
        @JsonProperty("necessaryConditions") List<NecessaryCondition> necessaryConditions) {
}
