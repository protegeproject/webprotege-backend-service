package edu.stanford.protege.webprotege.frame;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.change.HasApplyChanges;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 24/04/2013
 */
public class UpdateAnnotationPropertyFrameActionHandler extends AbstractUpdateFrameHandler<UpdateAnnotationPropertyFrameAction, UpdateAnnotationPropertyFrameResult> {


    @Nonnull
    private final PlainFrameRenderer plainFrameRenderer;

    @Inject
    public UpdateAnnotationPropertyFrameActionHandler(@Nonnull AccessManager accessManager,
                                                      @Nonnull HasApplyChanges applyChanges,
                                                      @Nonnull FrameChangeGeneratorFactory frameChangeGeneratorFactory,
                                                      @Nonnull PlainFrameRenderer plainFrameRenderer) {
        super(accessManager, applyChanges, frameChangeGeneratorFactory);
        this.plainFrameRenderer = plainFrameRenderer;
    }

    @Override
    protected UpdateAnnotationPropertyFrameResult createResponse(PlainEntityFrame to) {
        return new UpdateAnnotationPropertyFrameResult(plainFrameRenderer.toAnnotationPropertyFrame((PlainAnnotationPropertyFrame) to));
    }

    @Nonnull
    @Override
    public Class<UpdateAnnotationPropertyFrameAction> getActionClass() {
        return UpdateAnnotationPropertyFrameAction.class;
    }
}
