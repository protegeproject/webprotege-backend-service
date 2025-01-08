package edu.stanford.protege.webprotege.hierarchy;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.Response;

@JsonTypeName(GetHierarchyDescriptorRequest.CHANNEL)
public record GetHierarchyDescriptorResponse(@JsonProperty("descriptor") HierarchyDescriptor hierarchyDescriptor) implements Response {
}
