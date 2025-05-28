package edu.stanford.protege.webprotege.hierarchy;

import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.Response;

@JsonTypeName(SetProjectHierarchyDescriptorRulesRequest.CHANNEL)
public record SetProjectHierarchyDescriptorRulesResponse() implements Response {

}
