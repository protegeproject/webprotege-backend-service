package edu.stanford.protege.webprotege.hierarchy.ordering.dtos;

import com.fasterxml.jackson.annotation.*;

import java.util.List;

public record OrderedChildren(@JsonProperty("orderedChildren") List<OrderedChild> orderedChildren,
                              @JsonProperty("entityURI") String entityUri) {
}
