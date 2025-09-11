package edu.stanford.protege.webprotege.hierarchy;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.ChangeRequestId;
import edu.stanford.protege.webprotege.common.ContentChangeRequest;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.entity.EntityNode;

import javax.annotation.Nonnull;

@JsonTypeName("webprotege.hierarchies.MoveHierarchyNodeIcd")
public record MoveHierarchyNodeIcdAction(ChangeRequestId changeRequestId, @Nonnull ProjectId projectId, @Nonnull HierarchyDescriptor hierarchyDescriptor, @Nonnull Path<EntityNode> fromNodePath, @Nonnull Path<EntityNode> toNodeParentPath, @Nonnull DropType dropType, String commitMessage) implements ProjectAction<MoveHierarchyNodeIcdResult>, ContentChangeRequest {
    public static final String CHANNEL = "webprotege.hierarchies.MoveHierarchyNodeIcd";

    public MoveHierarchyNodeIcdAction(@JsonProperty("changeRequestId") ChangeRequestId changeRequestId, @JsonProperty("projectId") @Nonnull ProjectId projectId, @JsonProperty("hierarchyDescriptor") @Nonnull HierarchyDescriptor hierarchyDescriptor, @JsonProperty("fromNodePath") @Nonnull Path<EntityNode> fromNodePath, @JsonProperty("toNodeParentPath") @Nonnull Path<EntityNode> toNodeParentPath, @JsonProperty("dropType") @Nonnull DropType dropType, @JsonProperty("commitMessage") String commitMessage) {
        this.changeRequestId = changeRequestId;
        this.projectId = projectId;
        this.hierarchyDescriptor = hierarchyDescriptor;
        this.fromNodePath = fromNodePath;
        this.toNodeParentPath = toNodeParentPath;
        this.dropType = dropType;
        this.commitMessage = commitMessage;
    }

    public String getChannel() {
        return "webprotege.hierarchies.MoveHierarchyNodeIcd";
    }

    @JsonProperty("changeRequestId")
    public ChangeRequestId changeRequestId() {
        return this.changeRequestId;
    }

    @JsonProperty("projectId")
    @Nonnull
    public ProjectId projectId() {
        return this.projectId;
    }

    @JsonProperty("hierarchyDescriptor")
    @Nonnull
    public HierarchyDescriptor hierarchyDescriptor() {
        return this.hierarchyDescriptor;
    }

    @JsonProperty("fromNodePath")
    @Nonnull
    public Path<EntityNode> fromNodePath() {
        return this.fromNodePath;
    }

    @JsonProperty("toNodeParentPath")
    @Nonnull
    public Path<EntityNode> toNodeParentPath() {
        return this.toNodeParentPath;
    }

    @JsonProperty("dropType")
    @Nonnull
    public DropType dropType() {
        return this.dropType;
    }

    @JsonProperty("commitMessage")
    public String commitMessage() {
        return this.commitMessage;
    }
}