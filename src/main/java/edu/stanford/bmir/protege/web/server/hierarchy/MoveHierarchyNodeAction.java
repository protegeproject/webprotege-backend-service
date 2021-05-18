package edu.stanford.bmir.protege.web.server.hierarchy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;


import edu.stanford.bmir.protege.web.server.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.server.entity.EntityNode;
import edu.stanford.bmir.protege.web.server.project.ProjectId;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 8 Dec 2017
 */
@AutoValue

@JsonTypeName("MoveHierarchyNode")
public abstract class MoveHierarchyNodeAction implements ProjectAction<MoveHierarchyNodeResult> {

    @JsonCreator
    public static MoveHierarchyNodeAction create(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                                 @JsonProperty("hierarchyId") @Nonnull HierarchyId hierarchyId,
                                                 @JsonProperty("fromNodePath") @Nonnull Path<EntityNode> fromNodePath,
                                                 @JsonProperty("toNodeParentPath") @Nonnull Path<EntityNode> toNodeParentPath,
                                                 @JsonProperty("dropType") @Nonnull DropType dropType) {
        return new AutoValue_MoveHierarchyNodeAction(projectId, hierarchyId, fromNodePath, toNodeParentPath, dropType);
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    @Nonnull
    public abstract HierarchyId getHierarchyId();

    @Nonnull
    public abstract Path<EntityNode> getFromNodePath();

    @Nonnull
    public abstract Path<EntityNode> getToNodeParentPath();

    @Nonnull
    public abstract DropType getDropType();
}
