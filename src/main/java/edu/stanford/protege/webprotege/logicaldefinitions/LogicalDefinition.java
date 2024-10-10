package edu.stanford.protege.webprotege.logicaldefinitions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.entity.OWLClassData;
import edu.stanford.protege.webprotege.frame.PlainPropertyClassValue;
import edu.stanford.protege.webprotege.frame.PropertyClassValue;
import org.semanticweb.owlapi.model.IRI;

import java.util.List;


public record LogicalDefinition (
        @JsonProperty("logicalDefinitionParent") OWLClassData logicalDefinitionParent,
        @JsonProperty("axis2filler") List<PropertyClassValue> axis2filler
        ){
}
