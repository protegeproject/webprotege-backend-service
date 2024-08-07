package edu.stanford.protege.webprotege.icd.actions;



import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class AncestorHierarchyNode<T> {

    private List<AncestorHierarchyNode<T>> children = new ArrayList<>();
    private T node;

    @JsonProperty("children")
    public List<AncestorHierarchyNode<T>> getChildren() {
        return children;
    }

    public void setChildren(@JsonProperty("children") List<AncestorHierarchyNode<T>> children) {
        this.children = children;
    }

    @JsonProperty("currentNode")
    public T getNode() {
        return node;
    }

    public void setNode(@JsonProperty("currentNode") T node) {
        this.node = node;
    }
}
