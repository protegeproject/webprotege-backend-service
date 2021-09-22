package edu.stanford.protege.webprotege.perspective;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.common.base.MoreObjects;

import javax.annotation.Nonnull;
import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-20
 */
public class ParentNode extends Node {

    private final Direction direction;

    private final List<ParentNode.NodeHolder> children = new ArrayList<ParentNode.NodeHolder>();

    public ParentNode() {
        this(Direction.getDefaultDirection());
    }

    public ParentNode(Direction direction) {
        this.direction = checkNotNull(direction);
    }

    @JsonCreator
    protected ParentNode(@JsonProperty("direction") Direction direction,
                         @JsonProperty("children") List<ParentNode.NodeHolder> children) {
        this.direction = checkNotNull(direction);
        this.children.addAll(children);
    }

    @Override
    public String toString() {
        MoreObjects.ToStringHelper helper = MoreObjects.toStringHelper("ParentNode");
        helper.add("direction", direction);
        for (ParentNode.NodeHolder nodeHolder : children) {
            helper.add("weight", nodeHolder.getWeight());
            helper.addValue(nodeHolder.getNode());
        }
        return helper.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ParentNode)) {
            return false;
        }
        ParentNode other = (ParentNode) o;
        return this.getDirection() == other.getDirection() && this.children.equals(other.children);
    }

    @Override
    public boolean equalsIgnoreWeights(Node o) {
        if(o == this) {
            return true;
        }
        if(!(o instanceof ParentNode)) {
            return false;
        }
        ParentNode other = (ParentNode) o;
        if(this.getDirection() != other.getDirection()) {
            return false;
        }
        int childCount = getChildCount();
        if(childCount != other.getChildCount()) {
            return false;
        }
        for(int i = 0; i < childCount; i++) {
            Node myChild = getChildAt(i);
            Node otherChild = other.getChildAt(i);
            if(!myChild.equalsIgnoreWeights(otherChild)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return "ParentNode".hashCode() + direction.hashCode() + children.hashCode();
    }

    @Override
    public <O> O accept(NodeVisitor<O> visitor) {
        return visitor.visit(this);
    }

    public Node getChildAt(int index) {
        return children.get(index).getNode();
    }

    @JsonIgnore
    public int getChildCount() {
        return children.size();
    }

    @Nonnull
    public List<ParentNode.NodeHolder> getChildren() {
        return new ArrayList<>(children);
    }

    @JsonIgnore
    @Override
    public Set<TerminalNode> getTerminalNodes() {
        Set<TerminalNode> result = new HashSet<TerminalNode>();
        for(ParentNode.NodeHolder holder : children) {
            result.addAll(holder.getNode().getTerminalNodes());
        }
        return result;
    }

    public Direction getDirection() {
        return direction;
    }

    @JsonIgnore
    public double getTotalWeight() {
        double result = 0;
        for (ParentNode.NodeHolder wn : children) {
            result += wn.getWeight();
        }
        return result;
    }

    public double getWeightAt(int index) {
        return children.get(index).getWeight();
    }

    public void setWeightAt(int index, double weight) {
        children.get(index).setWeight(weight);
    }

    @Override
    public boolean isIsometricWith(Node otherNode) {
        if(otherNode == this) {
            return true;
        }
        if(!(otherNode instanceof ParentNode)) {
            return false;
        }
        ParentNode otherParent = (ParentNode) otherNode;
        if(getDirection() != otherParent.getDirection()) {
            return false;
        }
        int childCount = getChildCount();
        if(childCount != otherParent.getChildCount()) {
            return false;
        }
        for(int i = 0; i < childCount; i++) {
            double myChildWeight = getWeightAt(i);
            double otherChildWeight = otherParent.getWeightAt(i);
            if(myChildWeight != otherChildWeight) {
                return false;
            }
            Node myChild = getChildAt(i);
            Node otherChild = otherParent.getChildAt(i);
            if(!myChild.isIsometricWith(otherChild)) {
                return false;
            }
        }
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isParentNode() {
        return true;
    }

    public void addChild(Node child, double weight) {
        checkArgument(weight >= 0);
        children.add(new ParentNode.NodeHolder(checkNotNull(child), weight));
        child.setParent(Optional.of(this));
    }

    public void removeChild(Node child) {
        int index = indexOf(child);
        if (index != -1) {
            children.remove(index);
            child.setParent(Optional.empty());
        }
    }

    @JsonIgnore
    @Override
    public ParentNode duplicate() {
        ParentNode parentNode = new ParentNode(direction);
        for (ParentNode.NodeHolder child : children) {
            Node childDuplicate = child.getNode().duplicate();
            parentNode.addChild(childDuplicate, child.getWeight());
        }
        return parentNode;
    }

    @Override
    public Node minimise() {
        if (children.size() == 1) {
            return getFirstNodeMinimised();
        }
        else {
            return minimiseAllChildren();
        }
    }

    public void replaceWith(TerminalNode node, Direction direction, TerminalNode... replacementNodes) {
        int index = indexOf(node);
        if (index == -1) {
            return;
        }
        ParentNode newParentNode = new ParentNode(direction);
        newParentNode.setParent(Optional.of(this));
        children.set(index, new ParentNode.NodeHolder(newParentNode, getWeightAt(index)));
        for (TerminalNode tn : replacementNodes) {
            double weight = 1.0 / replacementNodes.length;
            newParentNode.addChild(tn, weight);
        }
        minimise();
    }

    protected int indexOf(Node child) {
        for (int i = 0; i < children.size(); i++) {
            Node node = children.get(i).getNode();
            if (node.equals(child)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns a minimised version of the first child node.
     *
     * @return The first child node minimised.
     */
    @JsonIgnore
    @Nonnull
    private Node getFirstNodeMinimised() {
        return children.get(0).getNode().minimise();
    }

    /**
     * Minimised all of this nodes children and returns this node.
     *
     * @return This node, with all of the children minimised
     */
    private Node minimiseAllChildren() {
        List<ParentNode.NodeHolder> currentChildren = new ArrayList<ParentNode.NodeHolder>(children);
        final double currentTotalWeight = getTotalWeight();
        children.clear();
        for (ParentNode.NodeHolder child : currentChildren) {
            final Node existingChild = child.getNode();
            final Node minimisedChild = existingChild.minimise();
            if (minimisedChild.isParentNode() && ((ParentNode) minimisedChild).getDirection() == this.getDirection()) {
                double childWeight = child.getWeight();
                double weightProportion = childWeight / currentTotalWeight;
                final ParentNode node = (ParentNode) minimisedChild;
                for (int i = 0; i < node.getChildCount(); i++) {
                    Node grandChildNode = node.getChildAt(i);
                    double grandChildWeight = node.getWeightAt(i);
                    double reProportionedWeight = grandChildWeight * weightProportion;
                    addChild(grandChildNode, reProportionedWeight);
                }
                minimisedChild.setParent(Optional.empty());
            }
            else {
                double childWeight = child.getWeight();
                addChild(minimisedChild, childWeight);
            }
        }
        return this;
    }

    @JsonPropertyOrder({ParentNode.NodeHolder.WEIGHT, ParentNode.NodeHolder.NODE})
    public static class NodeHolder {

        static final String NODE = "node";

        static final String WEIGHT = "weight";

        private Node node;

        private double weight;

        @SuppressWarnings("unused")
        private NodeHolder() {
        }

        @JsonCreator
        private NodeHolder(@JsonProperty(NODE) Node node, @JsonProperty(WEIGHT) double weight) {
            this.node = node;
            this.weight = weight;
        }

        @JsonProperty(NODE)
        public Node getNode() {
            return node;
        }

        @JsonProperty(WEIGHT)
        public double getWeight() {
            return weight;
        }

        private void setWeight(double weight) {
            this.weight = weight;
        }

        @Override
        public int hashCode() {
            return "NodeHolder".hashCode() + node.hashCode() + (int) weight * 100;
        }

        @Override
        public boolean equals(Object o) {
            if(o == this) {
                return true;
            }
            if(!(o instanceof ParentNode.NodeHolder)) {
                return false;
            }
            ParentNode.NodeHolder other = (ParentNode.NodeHolder) o;
            return this.node.equals(other.node) && this.weight == other.weight;
        }
    }
}
