package edu.stanford.protege.webprotege.hierarchy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-20
 */
public class RemoveEdge<U> extends EdgeChange<U> {

    @JsonCreator
    public RemoveEdge(@JsonProperty("edge") GraphEdge<U> edge) {
        super(edge);
    }

    @Override
    public void accept(GraphModelChangeVisitor<U> visitor) {
        visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return "RemoveEdge".hashCode() + getEdge().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(!(o instanceof RemoveEdge)) {
            return false;
        }
        RemoveEdge other = (RemoveEdge) o;
        return this.getEdge().equals(other.getEdge());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("RemoveEdge")
                          .addValue(getEdge())
                          .toString();
    }
}
