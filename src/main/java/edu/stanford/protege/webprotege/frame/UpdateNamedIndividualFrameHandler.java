package edu.stanford.protege.webprotege.frame;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.change.HasApplyChanges;
import edu.stanford.protege.webprotege.dispatch.Action;
import edu.stanford.protege.webprotege.event.EventList;
import edu.stanford.protege.webprotege.common.ProjectEvent;
import edu.stanford.protege.webprotege.events.EventManager;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Comparator;

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
                                             @Nonnull EventManager<ProjectEvent> eventManager,
                                             @Nonnull HasApplyChanges applyChanges,
                                             @Nonnull FrameChangeGeneratorFactory frameChangeGeneratorFactory,
                                             @Nonnull PlainFrameRenderer plainFrameRenderer) {
        super(accessManager, eventManager, applyChanges, frameChangeGeneratorFactory);
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
    protected UpdateNamedIndividualFrameResult createResponse(PlainEntityFrame to, EventList<ProjectEvent> events) {
        return new UpdateNamedIndividualFrameResult(plainFrameRenderer.toNamedIndividualFrame((PlainNamedIndividualFrame) to));
    }
}
