package edu.stanford.protege.webprotege.frame;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.change.HasApplyChanges;
import edu.stanford.protege.webprotege.dispatch.Action;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/02/2013
 */
public class UpdateNamedIndividualFrameHandler extends AbstractUpdateFrameHandler<UpdateNamedIndividualFrameAction, UpdateNamedIndividualFrameResult> {



    @Nonnull
    private final PlainFrameRenderer plainFrameRenderer;

    @Inject
    public UpdateNamedIndividualFrameHandler(@Nonnull AccessManager accessManager,
                                             @Nonnull HasApplyChanges applyChanges,
                                             @Nonnull FrameChangeGeneratorFactory frameChangeGeneratorFactory,
                                             @Nonnull PlainFrameRenderer plainFrameRenderer) {
        super(accessManager, applyChanges, frameChangeGeneratorFactory);
        this.plainFrameRenderer = plainFrameRenderer;
    }

    /**
     * Gets the class of {@link Action} handled by this handler.
     * @return The class of {@link Action}.  Not {@code null}.
     */
    @Nonnull
    @Override
    public Class<UpdateNamedIndividualFrameAction> getActionClass() {
        return UpdateNamedIndividualFrameAction.class;
    }

    @Override
    protected UpdateNamedIndividualFrameResult createResponse(PlainEntityFrame to) {
        return new UpdateNamedIndividualFrameResult(plainFrameRenderer.toNamedIndividualFrame((PlainNamedIndividualFrame) to));
    }
}
