package edu.stanford.protege.webprotege.hierarchy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.Objects;

public record ProjectHierarchyDescriptorRules(@JsonProperty("_id") @Id ProjectId projectId,
                                              @JsonProperty("rules") List<HierarchyDescriptorRule> rules) {

    @JsonCreator
    public ProjectHierarchyDescriptorRules(@JsonProperty("_id") ProjectId projectId,
                                           @JsonProperty("rules") List<HierarchyDescriptorRule> rules) {
        this.projectId = Objects.requireNonNull(projectId, "projectId cannot be null");
        this.rules = Objects.requireNonNull(rules, "rules cannot be null");
    }
}
