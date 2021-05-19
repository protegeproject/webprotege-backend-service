package edu.stanford.protege.webprotege.hierarchy;

import com.google.common.base.MoreObjects;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-20
 */
public class UpdateUserObject<U> extends GraphModelChange<U> {

    private U userObject;

    private UpdateUserObject() {
    }

    public UpdateUserObject(@Nonnull U userObject) {
        this.userObject = checkNotNull(userObject);
    }

    @Nonnull
    public U getUserObject() {
        return userObject;
    }

    @Override
    public void accept(GraphModelChangeVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    void forEachGraphNode(Consumer<GraphNode<U>> nodeConsumer) {

    }

    @Override
    public int hashCode() {
        return "UpdateUserObject".hashCode() + userObject.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(!(o instanceof UpdateUserObject)) {
            return false;
        }
        UpdateUserObject other = (UpdateUserObject) o;
        return this.userObject.equals(other.userObject);
    }

    @Override
    public String toString() {
        return MoreObjects
                .toStringHelper("UpdateUserObject")
                .addValue(userObject)
                .toString();
    }
}
