package edu.stanford.protege.webprotege.hierarchy;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.ProjectRequest;

@JsonTypeName(GetHierarchyDescriptorRequest.CHANNEL)
public record GetHierarchyDescriptorRequest(@JsonProperty("projectId") ProjectId projectId,
                                            @JsonProperty("displayContext") DisplayContext displayContext) implements ProjectRequest<GetHierarchyDescriptorResponse> {

    public static final String CHANNEL = "webprotege.hierarchies.GetHierarchyDescriptor";

    @Override
    public String getChannel() {
        return CHANNEL;
    }
}
