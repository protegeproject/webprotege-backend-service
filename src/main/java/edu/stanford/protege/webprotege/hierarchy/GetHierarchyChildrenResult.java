package edu.stanford.protege.webprotege.hierarchy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.entity.EntityNode;
import edu.stanford.protege.webprotege.pagination.Page;

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
