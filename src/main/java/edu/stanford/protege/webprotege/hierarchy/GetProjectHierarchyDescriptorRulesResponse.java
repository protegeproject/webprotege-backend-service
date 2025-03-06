package edu.stanford.protege.webprotege.hierarchy;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.Response;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;

@JsonTypeName(GetProjectHierarchyDescriptorRulesRequest.CHANNEL)
public record GetProjectHierarchyDescriptorRulesResponse(@JsonProperty("rules") @Nonnull List<HierarchyDescriptorRule> rules) implements Response {

    public GetProjectHierarchyDescriptorRulesResponse(@JsonProperty("rules") @Nonnull List<HierarchyDescriptorRule> rules) {
        this.rules = Objects.requireNonNull(rules);
    }
}
