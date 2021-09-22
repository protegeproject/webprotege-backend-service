package edu.stanford.protege.webprotege.perspective;

import com.fasterxml.jackson.annotation.*;
import com.google.common.base.MoreObjects;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-20
 */
@JsonPropertyOrder({"id", "properties"})
@JsonTypeName("LeafNode")
public class TerminalNode extends Node {

    private NodeProperties nodeProperties;

    private final TerminalNodeId nodeId;

    /**
     * Constructs a fresh {@code TerminalNode}.  A fresh {@link TerminalNodeId} will be generated for the node.
     */
    public TerminalNode() {
        this(TerminalNodeId.get());
    }

    /**
     * Constructs a {@code TerminalNode} that has the specified {@link TerminalNodeId}.
     *
     * @param nodeId The {@link TerminalNodeId}.  Not {@code null}.
     * @throws java.lang.NullPointerException if {@code nodeId} is {@code null}.
     */
    public TerminalNode(TerminalNodeId nodeId) {
        this(nodeId, NodeProperties.emptyNodeProperties());
    }

    @JsonCreator
    public TerminalNode(@JsonProperty("id") TerminalNodeId nodeId,
                        @JsonProperty("properties") NodeProperties nodeProperties) {
        if(nodeId == null) {
            this.nodeId = TerminalNodeId.get();
        }
        else {
            this.nodeId = nodeId;
        }
        if(nodeProperties == null) {
            this.nodeProperties = NodeProperties.emptyNodeProperties();
        }
        else {
            this.nodeProperties = checkNotNull(nodeProperties);
        }
    }

    /**
     * Gets the {@link TerminalNodeId} for this node.
     *
     * @return The {@link TerminalNodeId}.  Not {@code null}.
     */
    @JsonProperty("id")
    public TerminalNodeId getNodeId() {
        return nodeId;
    }

    /**
     * Gets this node's {@link NodeProperties}.
     *
     * @return The {@link NodeProperties} for this node.  Not {@code null}.
     */
    @JsonProperty("properties")
    public NodeProperties getNodeProperties() {
        return nodeProperties;
    }

    /**
     * Sets this nodes properties
     * @param nodeProperties The node properties.
     */
    public void setNodeProperties(@Nonnull NodeProperties nodeProperties) {
        this.nodeProperties = checkNotNull(nodeProperties);
    }

    @Override
    public <O> O accept(NodeVisitor<O> visitor) {
        return visitor.visit(this);
    }

    @Override
    public TerminalNode duplicate() {
        TerminalNode result = new TerminalNode(nodeId);
        result.setParent(getParent());
        result.nodeProperties = nodeProperties;
        return result;
    }

    @Override
    public boolean isIsometricWith(Node otherNode) {
        return otherNode instanceof TerminalNode;
    }

    @JsonIgnore
    @Override
    public boolean isParentNode() {
        return false;
    }

    @Override
    public Node minimise() {
        return this;
    }

    @JsonIgnore
    @Override
    public Set<TerminalNode> getTerminalNodes() {
        return Collections.singleton(this);
    }

    @Override
    public int hashCode() {
        return "TerminalNode".hashCode() + nodeId.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TerminalNode)) {
            return false;
        }
        TerminalNode other = (TerminalNode) o;
        return this.nodeId.equals(other.nodeId);
    }

    @Override
    public boolean equalsIgnoreWeights(Node other) {
        return equals(other);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("TerminalNode").addValue(nodeId).addValue(nodeProperties).toString();
    }
}
