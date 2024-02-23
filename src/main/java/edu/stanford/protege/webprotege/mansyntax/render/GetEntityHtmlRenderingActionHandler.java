package edu.stanford.protege.webprotege.mansyntax.render;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.renderer.GetEntityHtmlRenderingAction;
import edu.stanford.protege.webprotege.renderer.GetEntityHtmlRenderingResult;
import edu.stanford.protege.webprotege.renderer.RenderingManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-03-27
 */
public class GetEntityHtmlRenderingActionHandler extends AbstractProjectActionHandler<GetEntityHtmlRenderingAction, GetEntityHtmlRenderingResult> {

    @Nonnull
    private final ManchesterSyntaxEntityFrameRenderer renderer;

    @Nonnull
    private final RenderingManager renderingManager;

    @Inject
    public GetEntityHtmlRenderingActionHandler(@Nonnull AccessManager accessManager,
                                               @Nonnull ManchesterSyntaxEntityFrameRenderer renderer,
                                               @Nonnull RenderingManager renderingManager) {
        super(accessManager);
        this.renderer = checkNotNull(renderer);
        this.renderingManager = checkNotNull(renderingManager);
    }

    @Nonnull
    @Override
    public Class<GetEntityHtmlRenderingAction> getActionClass() {
        return GetEntityHtmlRenderingAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(GetEntityHtmlRenderingAction action) {
        return BuiltInAction.VIEW_PROJECT;
    }

    @Nonnull
    @Override
    public GetEntityHtmlRenderingResult execute(@Nonnull GetEntityHtmlRenderingAction action,
                                                @Nonnull ExecutionContext executionContext) {
        var sb = new StringBuilder();
        var entity = action.getEntity();
        renderer.render(entity, sb);
        var entityData = renderingManager.getRendering(entity);
        return GetEntityHtmlRenderingResult.create(entityData, sb.toString());
    }
}
