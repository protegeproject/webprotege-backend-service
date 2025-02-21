package edu.stanford.protege.webprotege.hierarchy;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.ProjectRequest;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonTypeName(GetProjectHierarchyDescriptorRulesRequest.CHANNEL)
public record GetProjectHierarchyDescriptorRulesRequest(@JsonProperty("projectId") ProjectId projectId) implements ProjectRequest<GetProjectHierarchyDescriptorRulesResponse> {

    public static final String CHANNEL = "webprotege.hierarchies.GetProjectHierarchyDescriptorRules";

    @Override
    public String getChannel() {
        return CHANNEL;
    }
}
