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
public class UpdateObjectPropertyFrameHandler extends AbstractUpdateFrameHandler<UpdateObjectPropertyFrameAction, UpdateObjectPropertyFrameResult> {

    @Nonnull
    private final PlainFrameRenderer plainFrameRenderer;

    @Inject
    public UpdateObjectPropertyFrameHandler(@Nonnull AccessManager accessManager,
                                            @Nonnull HasApplyChanges applyChanges,
                                            @Nonnull FrameChangeGeneratorFactory frameChangeGeneratorFactory,
                                            @Nonnull PlainFrameRenderer plainFrameRenderer) {
        super(accessManager, applyChanges, frameChangeGeneratorFactory);
        this.plainFrameRenderer = plainFrameRenderer;
    }

    @Override
    protected UpdateObjectPropertyFrameResult createResponse(PlainEntityFrame to) {
        return new UpdateObjectPropertyFrameResult(plainFrameRenderer.toObjectPropertyFrame((PlainObjectPropertyFrame) to));
    }

    @Nonnull
    @Override
    public Class<UpdateObjectPropertyFrameAction> getActionClass() {
        return UpdateObjectPropertyFrameAction.class;
    }
}
