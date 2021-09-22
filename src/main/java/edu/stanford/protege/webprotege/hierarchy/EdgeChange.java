package edu.stanford.protege.webprotege.hierarchy;

import java.util.function.Consumer;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-20
 */
public abstract class EdgeChange<U> extends GraphModelChange<U> {

    private final GraphEdge<U> edge;

    protected EdgeChange(GraphEdge<U> edge) {
        this.edge = edge;
    }

    public GraphNode<U> getPredecessor() {
        return edge.getPredecessor();
    }

    public GraphNode<U> getSuccessor() {
        return edge.getSuccessor();
    }

    public GraphEdge<U> getEdge() {
        return edge;
    }

    @Override
    void forEachGraphNode(Consumer<GraphNode<U>> nodeConsumer) {
        nodeConsumer.accept(edge.getPredecessor());
        nodeConsumer.accept(edge.getSuccessor());
    }
}
