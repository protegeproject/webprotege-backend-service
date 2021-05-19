package edu.stanford.protege.webprotege.frame;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.change.HasApplyChanges;
import edu.stanford.protege.webprotege.events.EventManager;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.dispatch.UpdateObjectResult;
import edu.stanford.protege.webprotege.event.EventList;
import edu.stanford.protege.webprotege.event.ProjectEvent;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public class UpdateObjectPropertyFrameHandler extends AbstractUpdateFrameHandler<UpdateObjectPropertyFrameAction, ObjectPropertyFrame> {

    @Inject
    public UpdateObjectPropertyFrameHandler(@Nonnull AccessManager accessManager,
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
    public Class<UpdateObjectPropertyFrameAction> getActionClass() {
        return UpdateObjectPropertyFrameAction.class;
    }
}
