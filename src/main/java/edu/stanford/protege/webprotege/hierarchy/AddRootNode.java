package edu.stanford.protege.webprotege.hierarchy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.function.Consumer;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-20
 */
public class AddRootNode<U> extends GraphModelChange<U> {

    private final GraphNode<U> rootNode;

    @JsonCreator
    public AddRootNode(@JsonProperty("node") GraphNode<U> rootNode) {
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
        return "AddRootNode".hashCode() + rootNode.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(!(o instanceof AddRootNode)) {
            return false;
        }
        AddRootNode other = (AddRootNode) o;
        return this.rootNode.equals(other.rootNode);
    }

    @Override
    public String toString() {
        return toStringHelper("AddRootNode").addValue(rootNode).toString();
    }

}
