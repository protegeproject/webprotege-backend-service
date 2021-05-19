package edu.stanford.protege.webprotege.hierarchy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

import java.util.function.Consumer;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-20
 */
public class RemoveRootNode<U> extends GraphModelChange<U> {

    private final GraphNode<U> rootNode;

    @JsonCreator
    public RemoveRootNode(@JsonProperty("node") GraphNode<U> rootNode) {
        this.rootNode = rootNode;
    }

    public GraphNode<U> getNode() {
        return rootNode;
    }

    @Override
    public void accept(GraphModelChangeVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    void forEachGraphNode(Consumer<GraphNode<U>> nodeConsumer) {
        nodeConsumer.accept(rootNode);
    }

    @Override
    public int hashCode() {
        return "RemoveRootNode".hashCode() + rootNode.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(!(o instanceof RemoveRootNode)) {
            return false;
        }
        RemoveRootNode other = (RemoveRootNode) o;
        return this.rootNode.equals(other.rootNode);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("RemoveRootNode").addValue(rootNode).toString();
    }

}
