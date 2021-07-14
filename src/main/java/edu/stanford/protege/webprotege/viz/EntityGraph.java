package edu.stanford.protege.webprotege.viz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;
import edu.stanford.protege.webprotege.entity.OWLEntityData;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;


/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Oct 2018
 */
@AutoValue

public abstract class EntityGraph {

    @JsonCreator
    @Nonnull
    public static EntityGraph create(@JsonProperty("root") OWLEntityData root,
                                     @JsonProperty("edges") ImmutableSet<Edge> edges,
                                     @JsonProperty("prunedToEdgeLimit") boolean isPrunedToEdgeLimit) {
        final ImmutableSet.Builder<OWLEntityData> builder = ImmutableSet.builder();
        final ImmutableSetMultimap.Builder<OWLEntityData, Edge> edgesByTailNode = ImmutableSetMultimap.builder();
        builder.add(root);
        edges.forEach(e -> {
            builder.add(e.getTail());
            builder.add(e.getHead());
            edgesByTailNode.put(e.getTail(), e);
        });
        ImmutableSet<OWLEntityData> nodes = builder.build();
        return new AutoValue_EntityGraph(root, nodes.size(), nodes, edges.size(), edges, isPrunedToEdgeLimit, edgesByTailNode.build());
    }

    @Nonnull
    public abstract OWLEntityData getRoot();

    @JsonIgnore
    public abstract int getNodeCount();

    @JsonIgnore
    @Nonnull
    public abstract ImmutableSet<OWLEntityData> getNodes();

    @JsonIgnore
    public abstract int getEdgeCount();

    @Nonnull
    public abstract ImmutableSet<Edge> getEdges();

    @JsonIgnore
    @Nonnull
    public OWLEntity getRootEntity() {
        return getRoot().getEntity();
    }

    public abstract boolean isPrunedToEdgeLimit();

    @JsonIgnore
    @Nonnull
    public abstract ImmutableSetMultimap<OWLEntityData, Edge> getEdgesByTailNode();

    @JsonIgnore
    public Set<OWLEntityData> getEdgeLabels() {
        if(getEdges().isEmpty()) {
            return ImmutableSet.of();
        }
        ImmutableSet.Builder<OWLEntityData> builder = ImmutableSet.builder();
        for(Edge edge : getEdges()) {
            edge.getLabellingEntity().ifPresent(builder::add);
        }
        return builder.build();
    }

    @JsonIgnore
    public ImmutableMultimap<OWLEntityData, String> getDescriptorsByTailNode() {
        ImmutableMultimap.Builder<OWLEntityData, String> result = ImmutableMultimap.builder();
        for (Edge edge : getEdges()) {
            result.put(edge.getTail(), edge.getRelationshipDescriptor());
        }
        return result.build();
    }

    @JsonIgnore
    public ImmutableMultimap<String, Edge> getEdgesByDescriptor() {
        ImmutableMultimap.Builder<String, Edge> result = ImmutableMultimap.builder();
        for (Edge edge : getEdges()) {
            result.put(edge.getRelationshipDescriptor(), edge);
        }
        return result.build();
    }

    @JsonIgnore
    public ImmutableSetMultimap<OWLEntityData, Edge> getEdgesByCluster(OWLEntity rootNode) {
        Set<OWLEntityData> processed = new HashSet<>();
        ImmutableSetMultimap.Builder<OWLEntityData, Edge> resultBuilder = ImmutableSetMultimap.builder();
        getEdges().forEach(e -> {
                               Set<OWLEntityData> tailClusters = new HashSet<>();
                               Set<OWLEntityData> headClusters = new HashSet<>();
                               if (!rootNode.equals(e.getTail().getEntity())) {
                                   getIsAClusters(tailClusters, e.getTail(), new HashSet<>());
                                   getIsAClusters(headClusters, e.getHead(), new HashSet<>());
                                   if(tailClusters.equals(headClusters)) {
                                       tailClusters.forEach(c -> resultBuilder.put(c, e));
                                   }
                               }
                           }
        );
        return resultBuilder.build();
    }

    @JsonIgnore
    public Set<OWLEntityData> getTransitiveClosure(OWLEntityData from, Set<OWLEntityData> edgeFilter) {
        Set<OWLEntityData> nodes = new HashSet<>();
        ImmutableMultimap<OWLEntityData, Edge> edgeMap = getEdgesByTailNode();
        getTransitiveClosure(from, nodes, edgeFilter, edgeMap);
        return nodes;
    }

    private void getTransitiveClosure(@Nonnull OWLEntityData from,
                                      @Nonnull Set<OWLEntityData> nodes,
                                      @Nonnull Set<OWLEntityData> edgeFilter,
                                      @Nonnull ImmutableMultimap<OWLEntityData, Edge> edgesByTailNode) {
        if(nodes.contains(from)) {
            return;
        }
        nodes.add(from);
        ImmutableCollection<Edge> edges = edgesByTailNode.get(from);
        for(Edge edge : edges) {
            if(isIncluded(edgeFilter, edge)) {
                getTransitiveClosure(edge.getHead(), nodes, edgeFilter, edgesByTailNode);
            }
        }
    }

    private boolean isIncluded(@Nonnull Set<OWLEntityData> edgeFilter, Edge edge) {
        if(edge.isIsA()) {
            return true;
        }
        else {
            OWLEntityData rel = ((RelationshipEdge) edge).getRelationship();
            return edgeFilter.contains(rel);
        }
    }

    private void getIsAClusters(Set<OWLEntityData> clutersBuilder,
                                OWLEntityData node,
                                Set<OWLEntityData> processed) {
        if (processed.contains(node)) {
            return;
        }
        processed.add(node);
        long isACount = getEdgesByTailNode().get(node)
                .stream()
                .filter(Edge::isIsA)
                .peek(e -> getIsAClusters(clutersBuilder, e.getHead(), processed))
                .count();
        if (isACount == 0) {
            clutersBuilder.add(node);
        }

    }

}
