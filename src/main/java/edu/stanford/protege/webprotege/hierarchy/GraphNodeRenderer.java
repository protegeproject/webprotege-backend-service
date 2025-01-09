package edu.stanford.protege.webprotege.hierarchy;

import edu.stanford.protege.webprotege.entity.EntityNode;
import edu.stanford.protege.webprotege.entity.EntityNodeRenderer;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 19 Dec 2017
 */
public class GraphNodeRenderer {

    @Nonnull
    private final EntityNodeRenderer renderer;

    @Inject
    public GraphNodeRenderer(@Nonnull EntityNodeRenderer renderer) {
        this.renderer = checkNotNull(renderer);
    }

    /**
     * Render the specified entity into a {@link GraphNode} whose user object
     * is and {@link EntityNode}.
     * @param entity The entity to be rendered.
     * @param hierarchyProvider A hierarchy that is used to provide information.
     */
    public GraphNode<EntityNode> toGraphNode(@Nonnull OWLEntity entity,
                                 @Nonnull HierarchyProvider<OWLEntity> hierarchyProvider) {
        return new GraphNode<EntityNode>(renderer.render(entity), hierarchyProvider.isLeaf(entity));
    }
}
