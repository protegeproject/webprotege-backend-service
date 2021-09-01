package edu.stanford.protege.webprotege.events;



import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.entity.EntityNode;
import edu.stanford.protege.webprotege.entity.EntityNodeRenderer;
import edu.stanford.protege.webprotege.common.ProjectEvent;
import edu.stanford.protege.webprotege.hierarchy.*;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-11-05
 */
public class EntityHierarchyChangedEventProxy implements HighLevelProjectEventProxy {

    @Nonnull
    private final GraphModelChangedEvent<OWLEntity> graphModelChangedEvent;

    @Nonnull
    private final GraphNodeRenderer renderer;

    @Nonnull
    private final EntityNodeRenderer entityNodeRenderer;

    @Nonnull
    private final HierarchyProvider<OWLEntity> hierarchyProvider;

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final HierarchyId hierarchyId;


    public EntityHierarchyChangedEventProxy(@Nonnull GraphModelChangedEvent<? extends OWLEntity> graphModelChangedEvent,
                                            @Nonnull GraphNodeRenderer renderer,
                                            @Nonnull EntityNodeRenderer entityNodeRenderer,
                                            @Nonnull HierarchyProvider<? extends OWLEntity> classHierarchyProvider,
                                            @Nonnull ProjectId projectId,
                                            @Nonnull HierarchyId hierarchyId) {
        this.graphModelChangedEvent = (GraphModelChangedEvent) graphModelChangedEvent;
        this.renderer = renderer;
        this.entityNodeRenderer = entityNodeRenderer;
        this.hierarchyProvider = (HierarchyProvider) classHierarchyProvider;
        this.projectId = projectId;
        this.hierarchyId = hierarchyId;
    }

    @Nonnull
    @Override
    public ProjectEvent asProjectEvent() {
        var mappedChanges = ImmutableList.<GraphModelChange<EntityNode>>builder();
        graphModelChangedEvent.getChanges()
             .forEach(chg -> chg.accept(new GraphModelChangeVisitor<OWLEntity>() {
                 @Override
                 public void visit(AddRootNode<OWLEntity> addRootNode) {
                     var entity = addRootNode.getNode().getUserObject();
                     var mappedChg = new AddRootNode<>(
                             renderer.toGraphNode(entity, hierarchyProvider));
                     mappedChanges.add(mappedChg);
                 }

                 @Override
                 public void visit(RemoveRootNode<OWLEntity> removeRootNode) {
                     var entity = removeRootNode.getNode().getUserObject();
                     var mappedChg = new RemoveRootNode<>(
                             renderer.toGraphNode(entity, hierarchyProvider));
                     mappedChanges.add(mappedChg);
                 }

                 @Override
                 public void visit(AddEdge<OWLEntity> addEdge) {
                     var pred = addEdge.getPredecessor().getUserObject();
                     var succ = addEdge.getSuccessor().getUserObject();
                     var mappedChg = new AddEdge<>(
                                new GraphEdge<>(
                                        renderer.toGraphNode(pred, hierarchyProvider),
                                        renderer.toGraphNode(succ, hierarchyProvider)
                                )
                             );
                     mappedChanges.add(mappedChg);
                 }

                 @Override
                 public void visit(RemoveEdge<OWLEntity> removeEdge) {
                     var pred = removeEdge.getPredecessor().getUserObject();
                     var succ = removeEdge.getSuccessor().getUserObject();
                     var mappedChg = new RemoveEdge<>(
                             new GraphEdge<>(
                                     renderer.toGraphNode(pred, hierarchyProvider),
                                     renderer.toGraphNode(succ, hierarchyProvider)
                             )
                     );
                     mappedChanges.add(mappedChg);
                 }

                 @Override
                 public void visit(UpdateUserObject<OWLEntity> updateUserObject) {
                     var entity = updateUserObject.getUserObject();
                     var mappedChg = new UpdateUserObject<>(entityNodeRenderer.render(entity));
                     mappedChanges.add(mappedChg);
                 }
             }));
        var mappedEvent = GraphModelChangedEvent.create(mappedChanges.build());
        return new EntityHierarchyChangedEvent(projectId, hierarchyId, mappedEvent);
    }
}
