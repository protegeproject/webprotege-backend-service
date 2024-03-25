package edu.stanford.protege.webprotege.mansyntax.render;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.renderer.GetEntityRenderingAction;
import edu.stanford.protege.webprotege.renderer.GetEntityRenderingResult;
import edu.stanford.protege.webprotege.renderer.RenderingManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import static edu.stanford.protege.webprotege.access.BuiltInAction.VIEW_PROJECT;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 25/02/2014
 */
public class GetEntityRenderingActionHandler extends AbstractProjectActionHandler<GetEntityRenderingAction, GetEntityRenderingResult> {

    @Nonnull
    private final RenderingManager renderingManager;

    @Inject
    public GetEntityRenderingActionHandler(@Nonnull AccessManager accessManager,
                                           @Nonnull RenderingManager renderingManager) {
        super(accessManager);
        this.renderingManager = renderingManager;
    }

    @Nonnull
    @Override
    public Class<GetEntityRenderingAction> getActionClass() {
        return GetEntityRenderingAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(GetEntityRenderingAction action) {
        return VIEW_PROJECT;
    }

    @Nonnull
    @Override
    public GetEntityRenderingResult execute(@Nonnull GetEntityRenderingAction action,
                                            @Nonnull ExecutionContext executionContext) {
        var entity = action.getEntity();
        return GetEntityRenderingResult.create(renderingManager.getRendering(entity));
    }
}
