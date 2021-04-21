package edu.stanford.bmir.protege.web.shared.hierarchy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 30 Nov 2017
 */
@AutoValue

@JsonTypeName("GetHierarchyRoots")
public abstract class GetHierarchyRootsResult implements Result {

    @JsonCreator
    public static GetHierarchyRootsResult create(@JsonProperty("rootNodes") @Nonnull List<GraphNode<EntityNode>> rootNodes) {
        return new AutoValue_GetHierarchyRootsResult(ImmutableList.copyOf(rootNodes));
    }

    public static GetHierarchyRootsResult empty() {
        return create(ImmutableList.of());
    }

    @Nonnull
    public abstract ImmutableList<GraphNode<EntityNode>> getRootNodes();
}
