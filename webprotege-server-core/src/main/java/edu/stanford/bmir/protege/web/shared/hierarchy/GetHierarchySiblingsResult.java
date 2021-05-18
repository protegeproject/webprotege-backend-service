package edu.stanford.bmir.protege.web.shared.hierarchy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;


import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.pagination.Page;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Sep 2018
 */
@AutoValue

@JsonTypeName("GetHierarchySiblings")
public abstract class GetHierarchySiblingsResult implements Result {

    @JsonCreator
    public static GetHierarchySiblingsResult create(@JsonProperty("siblings") Page<GraphNode<EntityNode>> siblingsPage) {
        return new AutoValue_GetHierarchySiblingsResult(siblingsPage);
    }

    public abstract Page<GraphNode<EntityNode>> getSiblings();
}