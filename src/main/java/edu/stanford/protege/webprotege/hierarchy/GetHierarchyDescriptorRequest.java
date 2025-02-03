package edu.stanford.protege.webprotege.hierarchy;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.ProjectRequest;
import edu.stanford.protege.webprotege.ui.DisplayContext;

import javax.annotation.Nonnull;
import java.util.Objects;

@JsonTypeName(GetHierarchyDescriptorRequest.CHANNEL)
public record GetHierarchyDescriptorRequest(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                            @JsonProperty("displayContext") DisplayContext displayContext) implements ProjectRequest<GetHierarchyDescriptorResponse> {

    public static final String CHANNEL = "webprotege.hierarchies.GetHierarchyDescriptor";

    public GetHierarchyDescriptorRequest(@JsonProperty("projectId") ProjectId projectId, @JsonProperty("displayContext") DisplayContext displayContext) {
        this.projectId = Objects.requireNonNull(projectId);
        this.displayContext = Objects.requireNonNull(displayContext);
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }
}
