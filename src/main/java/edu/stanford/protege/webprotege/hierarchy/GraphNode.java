package edu.stanford.protege.webprotege.hierarchy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import edu.stanford.protege.webprotege.entity.EntityNode;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-20
 */
public class GraphNode<U> {

    private final U userObject;

    private final boolean sink;

    public GraphNode(@Nonnull U userObject) {
        this(userObject, false);
    }

    public GraphNode(@Nonnull U userObject,
                     boolean sink) {
        this.userObject = checkNotNull(userObject);
        this.sink = sink;
    }

    @JsonCreator
    public static GraphNode<EntityNode> getForEntityNode(@JsonProperty("userObject") @Nonnull EntityNode userObject,
                     @JsonProperty(value = "sink", defaultValue = "false") boolean sink) {
        return new GraphNode<>(userObject, sink);
    }

    public static <U> GraphNode<U> get(@Nonnull U userObject) {
        return new GraphNode<>(userObject);
    }

    public static <U> GraphNode get(@Nonnull U userObject, boolean sink) {
        return new GraphNode<>(userObject, sink);
    }

    @Nonnull
    public U getUserObject() {
        return userObject;
    }

    public boolean isSink() {
        return sink;
    }

    @Override
    public int hashCode() {
        return userObject.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof GraphNode)) {
            return false;
        }
        GraphNode other = (GraphNode) obj;
        return this.userObject.equals(other.userObject);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("GraphNode").addValue(userObject).toString();
    }

}
