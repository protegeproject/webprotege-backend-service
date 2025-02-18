package edu.stanford.protege.webprotege.hierarchy.ordering.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OrderedChild(@JsonProperty("orderedChild") String orderedChild,
                           @JsonProperty("orderedChildIndex") String orderedChildIndex) {

}
