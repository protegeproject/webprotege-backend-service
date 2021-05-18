package edu.stanford.bmir.protege.web.shared.hierarchy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;


import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;

import java.util.Collection;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 28 Nov 2017
 */
@AutoValue

@JsonTypeName("GetHierarchyPathsToRoot")
public abstract class GetHierarchyPathsToRootResult implements Result {

    @JsonCreator
    public static GetHierarchyPathsToRootResult create(@JsonProperty("paths") Collection<Path<GraphNode<EntityNode>>> paths) {
        return new AutoValue_GetHierarchyPathsToRootResult(paths);
    }

    public abstract Collection<Path<GraphNode<EntityNode>>> getPaths();
}