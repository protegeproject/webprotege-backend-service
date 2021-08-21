package edu.stanford.protege.webprotege.frame;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.change.HasApplyChanges;
import edu.stanford.protege.webprotege.event.EventList;
import edu.stanford.protege.webprotege.event.ProjectEvent;
import edu.stanford.protege.webprotege.events.EventManager;
import edu.stanford.protege.webprotege.frame.translator.DataPropertyFrameTranslator;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public class UpdateDataPropertyFrameHandler extends AbstractUpdateFrameHandler<UpdateDataPropertyFrameAction, UpdateDataPropertyFrameResult> {

    @Inject
    public UpdateDataPropertyFrameHandler(@Nonnull AccessManager accessManager,
                                          @Nonnull EventManager<ProjectEvent<?>> eventManager,
                                          @Nonnull HasApplyChanges applyChanges,
                                          @Nonnull Provider<DataPropertyFrameTranslator> translatorProvider,
                                          FrameChangeGeneratorFactory frameChangeGeneratorFactory) {
        super(accessManager, eventManager, applyChanges, frameChangeGeneratorFactory);
    }

    @Override
    protected UpdateDataPropertyFrameResult createResponse(PlainEntityFrame to, EventList<ProjectEvent<?>> events) {
        return new UpdateDataPropertyFrameResult();
    }

    @Nonnull
    @Override
    public Class<UpdateDataPropertyFrameAction> getActionClass() {
        return UpdateDataPropertyFrameAction.class;
    }
}
