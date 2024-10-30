package edu.stanford.protege.webprotege.logicaldefinitions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.Objects;
import edu.stanford.protege.webprotege.entity.OWLClassData;
import edu.stanford.protege.webprotege.frame.PlainPropertyClassValue;
import edu.stanford.protege.webprotege.frame.PropertyClassValue;
import org.apache.lucene.util.CollectionUtil;
import org.semanticweb.owlapi.model.IRI;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public record LogicalDefinition (
        @JsonProperty("logicalDefinitionParent") OWLClassData logicalDefinitionParent,
        @JsonProperty("axis2filler") List<PropertyClassValue> axis2filler
        ){

        @Override
        public boolean equals(Object obj) {
                if (obj == null || obj instanceof LogicalDefinition == false) {
                        return false;
                }

                LogicalDefinition ld = (LogicalDefinition) obj;

                if (ld.logicalDefinitionParent().equals(logicalDefinitionParent()) == false) {
                        return false;
                }

                return diff(axis2filler(), ld.axis2filler()).isEmpty() && diff(ld.axis2filler(), axis2filler()).isEmpty();
        }


        public int hashCode() {
                return Objects.hashCode(logicalDefinitionParent, axis2filler);
        }

        private List<PropertyClassValue> diff(List<PropertyClassValue> listOne, List<PropertyClassValue> listTwo) {
                return listOne.stream()
                        .filter(two -> listTwo.stream().
                                noneMatch(one -> one.getProperty().equals(two.getProperty()) &&
                                        one.getValue().equals(two.getValue())))
                        .collect(Collectors.toList());
        }
}
