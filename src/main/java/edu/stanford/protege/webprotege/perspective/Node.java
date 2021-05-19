package edu.stanford.protege.webprotege.perspective;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-20
 */
@JsonSubTypes({@JsonSubTypes.Type(TerminalNode.class), @JsonSubTypes.Type(ParentNode.class)})
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
public abstract class Node {

    @Nullable
    private ParentNode parent = null;

    protected Node() {
    }

    public abstract <O> O accept(NodeVisitor<O> visitor);

    public abstract Node duplicate();

    public abstract Set<TerminalNode> getTerminalNodes();

    @JsonIgnore
    public abstract boolean isIsometricWith(Node otherNode);

    public abstract boolean equalsIgnoreWeights(Node other);

    @JsonIgnore
    public abstract boolean isParentNode();

    public abstract Node minimise();


    /**
     * Gets the parent of this node.
     *
     * @return The optional parent of this node.  Not {@code null}.  If this node does not have any parent then
     * {@link com.google.common.base.Optional#absent()} is returned.
     */
    @JsonIgnore
    public Optional<ParentNode> getParent() {
        return Optional.ofNullable(parent);
    }

    /**
     * Should only be called by {@link ParentNode} objects to set themselves
     * as the parent of this {@link Node}.
     *
     * @param parent The parent.  Not {@code null}.
     * @throws java.lang.NullPointerException is {@code parent} is {@code null}.
     */
    protected void setParent(Optional<ParentNode> parent) {
        this.parent = checkNotNull(parent).orElse(null);
    }

    /**
     * Removes this node from its parent node.
     */
    public void removeFromParent() {
        if (parent != null) {
            parent.removeChild(this);
        }
    }

}
