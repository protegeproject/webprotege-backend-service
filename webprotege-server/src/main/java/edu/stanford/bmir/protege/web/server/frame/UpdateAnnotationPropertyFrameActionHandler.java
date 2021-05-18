package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.change.HasApplyChanges;
import edu.stanford.bmir.protege.web.server.events.EventManager;
import edu.stanford.bmir.protege.web.server.dispatch.Result;
import edu.stanford.bmir.protege.web.server.dispatch.UpdateObjectResult;
import edu.stanford.bmir.protege.web.server.event.EventList;
import edu.stanford.bmir.protege.web.server.event.ProjectEvent;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 24/04/2013
 */
public class UpdateAnnotationPropertyFrameActionHandler extends AbstractUpdateFrameHandler<UpdateAnnotationPropertyFrameAction, AnnotationPropertyFrame> {

    @Inject
    public UpdateAnnotationPropertyFrameActionHandler(@Nonnull AccessManager accessManager,
                                                      @Nonnull EventManager<ProjectEvent<?>> eventManager,
                                                      @Nonnull HasApplyChanges applyChanges,
                                                      @Nonnull FrameChangeGeneratorFactory frameChangeGeneratorFactory) {
        super(accessManager, eventManager, applyChanges, frameChangeGeneratorFactory);
    }

    @Override
    protected Result createResponse(PlainEntityFrame to, EventList<ProjectEvent<?>> events) {
        return new UpdateObjectResult(events);
    }

    @Nonnull
    @Override
    public Class<UpdateAnnotationPropertyFrameAction> getActionClass() {
        return UpdateAnnotationPropertyFrameAction.class;
    }
}
