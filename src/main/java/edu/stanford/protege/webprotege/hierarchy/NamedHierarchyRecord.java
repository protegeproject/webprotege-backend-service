package edu.stanford.protege.webprotege.hierarchy;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.stanford.protege.webprotege.common.ProjectId;

import javax.annotation.Nonnull;

public record NamedHierarchyRecord(@JsonProperty("projectId") ProjectId projectId,
                                   @JsonProperty("namedHierarchy") NamedHierarchy namedHierarchy) {

}
