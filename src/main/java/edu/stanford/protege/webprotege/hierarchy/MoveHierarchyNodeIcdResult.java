package edu.stanford.protege.webprotege.hierarchy;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.dispatch.Result;

@JsonTypeName(MoveHierarchyNodeIcdAction.CHANNEL)
public record MoveHierarchyNodeIcdResult(@JsonProperty("moved") boolean moved,
                                         @JsonProperty("isDestinationRetiredClass") boolean isDestinationRetiredClass,
                                         @JsonProperty("isInitialParentLinPathParent") boolean isInitialParentLinPathParent) implements Result {

}
