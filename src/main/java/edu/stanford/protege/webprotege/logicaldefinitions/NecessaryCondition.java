package edu.stanford.protege.webprotege.logicaldefinitions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.frame.PlainPropertyClassValue;

import java.util.List;


public record NecessaryCondition (
      @JsonProperty("axis2filler") List<PlainPropertyClassValue> axis2filler
    ) {
}
