package edu.stanford.protege.webprotege.hierarchy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.Response;

import javax.annotation.Nullable;
import java.util.Optional;

@JsonTypeName(GetHierarchyDescriptorRequest.CHANNEL)
public record GetHierarchyDescriptorResponse(@Nullable @JsonProperty("hierarchyDescriptor") HierarchyDescriptor hierarchyDescriptor) implements Response {

    /**
     * Gets an Optional containing the HierarchyDescriptor.
     */
    @JsonIgnore
    public Optional<HierarchyDescriptor> getHierarchyDescriptor() {
        return Optional.ofNullable(hierarchyDescriptor);
    }
}
