package edu.stanford.protege.webprotege.hierarchy;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.ProjectRequest;

import java.util.List;

import static edu.stanford.protege.webprotege.hierarchy.SetProjectHierarchyDescriptorRulesRequest.CHANNEL;

@JsonTypeName(CHANNEL)
public record SetProjectHierarchyDescriptorRulesRequest(@JsonProperty("projectId") ProjectId projectId,
                                                        @JsonProperty("rules") List<HierarchyDescriptorRule> rules) implements ProjectRequest<SetProjectHierarchyDescriptorRulesResponse> {

    public static final String CHANNEL = "webprotege.hierarchies.SetProjectHierarchyDescriptorRules";

    @Override
    public String getChannel() {
        return CHANNEL;
    }
}
