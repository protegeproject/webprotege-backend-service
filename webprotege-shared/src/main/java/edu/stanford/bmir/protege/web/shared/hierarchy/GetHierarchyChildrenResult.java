package edu.stanford.bmir.protege.web.shared.hierarchy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.protege.gwt.graphtree.shared.graph.GraphNode;
import edu.stanford.protege.gwt.graphtree.shared.graph.SuccessorMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 28 Nov 2017
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("GetHierarchyChildren")
public abstract class GetHierarchyChildrenResult implements Result {

    public static GetHierarchyChildrenResult create() {
        return create(null, Page.emptyPage());
    }

    @JsonCreator
    public static GetHierarchyChildrenResult create(@JsonProperty("parent") @Nullable GraphNode<EntityNode> parent,
                                                    @JsonProperty("children") @Nonnull Page<GraphNode<EntityNode>> children) {
        return new AutoValue_GetHierarchyChildrenResult(parent, children);
    }

    @Nullable
    public abstract GraphNode<EntityNode> getParent();

    @Nonnull
    public abstract Page<GraphNode<EntityNode>> getChildren();

    public SuccessorMap<EntityNode> getSuccessorMap() {
        if(getParent() == null) {
            return SuccessorMap.<EntityNode>builder().build();
        }
        SuccessorMap.Builder<EntityNode> builder = SuccessorMap.builder();
        getChildren().getPageElements().forEach(child -> builder.add(getParent(), child));
        return builder.build();
    }
}
