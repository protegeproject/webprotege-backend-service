package edu.stanford.protege.webprotege.hierarchy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

import java.io.Serializable;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-20
 */
public class GraphEdge<U> implements Serializable {

    private final GraphNode<U> predecessor;

    private final GraphNode<U> successor;

    @JsonCreator
    public GraphEdge(@JsonProperty("predecessor") GraphNode<U> predecessor,
                     @JsonProperty("successor") GraphNode<U> successor) {
        this.predecessor = predecessor;
        this.successor = successor;
    }

    public GraphNode<U> getPredecessor() {
        return predecessor;
    }

    public GraphNode<U> getSuccessor() {
        return successor;
    }

    @Override
    public int hashCode() {
        return "GraphEdge".hashCode() + predecessor.hashCode() + 13 * successor.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(!(o instanceof GraphEdge)) {
            return false;
        }
        GraphEdge other = (GraphEdge) o;
        return this.predecessor.equals(other.predecessor) && this.successor.equals(other.getSuccessor());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("Edge")
                          .add("predecessor", predecessor)
                          .add("successor", successor)
                          .toString();
    }
}
