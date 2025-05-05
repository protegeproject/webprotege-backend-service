package edu.stanford.protege.webprotege.entity;

import edu.stanford.protege.webprotege.access.*;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.hierarchy.ClassHierarchyProvider;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.renderer.RenderingManager;
import jakarta.inject.Inject;

import javax.annotation.*;

import java.util.*;

import static edu.stanford.protege.webprotege.access.BuiltInAction.VIEW_PROJECT;

public class GetEntityDirectParentsActionHandler extends AbstractProjectActionHandler<GetEntityDirectParentsAction, GetEntityDirectParentsResult> {

    @Nonnull
    private final ClassHierarchyProvider classHierarchyProvider;
    @Nonnull
    private final EntityNodeRenderer entityNodeRenderer;

    @Nonnull
    private final RenderingManager renderingManager;

    @Inject
    public GetEntityDirectParentsActionHandler(@Nonnull AccessManager accessManager,
                                               @Nonnull ClassHierarchyProvider classHierarchyProvider,
                                               @Nonnull EntityNodeRenderer entityNodeRenderer,
                                               @Nonnull RenderingManager renderingManager) {
        super(accessManager);
        this.classHierarchyProvider = classHierarchyProvider;
        this.entityNodeRenderer = entityNodeRenderer;
        this.renderingManager = renderingManager;
    }

    @Nonnull
    @Override
    public Class<GetEntityDirectParentsAction> getActionClass() {
        return GetEntityDirectParentsAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(GetEntityDirectParentsAction action) {
        return VIEW_PROJECT;
    }

    @Nonnull
    @Override
    public GetEntityDirectParentsResult execute(@Nonnull GetEntityDirectParentsAction action,
                                                @Nonnull ExecutionContext executionContext) {
        List<EntityNode> parents = new ArrayList<>();
        if(action.entity().isOWLClass()){
            parents = classHierarchyProvider.getParents(action.entity().asOWLClass())
                    .stream()
                    .map(entityNodeRenderer::render)
                    .toList();
        }

        var entityData = renderingManager.getRendering(action.entity());
        return new GetEntityDirectParentsResult(entityData, parents);
    }


}
