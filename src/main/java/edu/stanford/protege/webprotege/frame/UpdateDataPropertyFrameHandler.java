package edu.stanford.protege.webprotege.frame;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.change.HasApplyChanges;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public class UpdateDataPropertyFrameHandler extends AbstractUpdateFrameHandler<UpdateDataPropertyFrameAction, UpdateDataPropertyFrameResult> {

    @Nonnull
    private final PlainFrameRenderer plainFrameRenderer;

    @Inject
    public UpdateDataPropertyFrameHandler(@Nonnull AccessManager accessManager,
                                          @Nonnull HasApplyChanges applyChanges,
                                          FrameChangeGeneratorFactory frameChangeGeneratorFactory,
                                          @Nonnull PlainFrameRenderer plainFrameRenderer) {
        super(accessManager, applyChanges, frameChangeGeneratorFactory);
        this.plainFrameRenderer = plainFrameRenderer;
    }

    @Override
    protected UpdateDataPropertyFrameResult createResponse(PlainEntityFrame to) {
        return new UpdateDataPropertyFrameResult(plainFrameRenderer.toDataPropertyFrame((PlainDataPropertyFrame) to));
    }

    @Nonnull
    @Override
    public Class<UpdateDataPropertyFrameAction> getActionClass() {
        return UpdateDataPropertyFrameAction.class;
    }
}
