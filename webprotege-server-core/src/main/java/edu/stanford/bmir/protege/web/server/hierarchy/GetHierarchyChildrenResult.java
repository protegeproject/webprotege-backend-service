package edu.stanford.bmir.protege.web.server.hierarchy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;

import edu.stanford.bmir.protege.web.server.dispatch.Result;
import edu.stanford.bmir.protege.web.server.entity.EntityNode;
import edu.stanford.bmir.protege.web.server.pagination.Page;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 28 Nov 2017
 */
@AutoValue
@JsonTypeName("GetHierarchyChildren")
public abstract class GetHierarchyChildrenResult implements Result {

    public static GetHierarchyChildrenResult create() {
        return create(null, Page.emptyPage());
    }

    @JsonCreator
    public static GetHierarchyChildrenResult create(@JsonProperty("parent") @Nullable GraphNode parent,
                                                    @JsonProperty("children") @Nonnull Page<GraphNode<EntityNode>> children) {
        return new AutoValue_GetHierarchyChildrenResult(parent, children);
    }

    @Nullable
    public abstract GraphNode getParent();

    @Nonnull
    public abstract Page<GraphNode<EntityNode>> getChildren();
}
