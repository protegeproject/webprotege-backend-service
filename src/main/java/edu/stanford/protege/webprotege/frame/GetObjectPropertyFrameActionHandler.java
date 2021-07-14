package edu.stanford.protege.webprotege.frame;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.dispatch.ExecutionContext;
import edu.stanford.protege.webprotege.frame.translator.ObjectPropertyFrameTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Provider;

import static edu.stanford.protege.webprotege.access.BuiltInAction.VIEW_PROJECT;
import static edu.stanford.protege.webprotege.logging.Markers.BROWSING;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public class GetObjectPropertyFrameActionHandler extends AbstractProjectActionHandler<GetObjectPropertyFrameAction, GetObjectPropertyFrameResult> {

    private static Logger logger = LoggerFactory.getLogger(GetObjectPropertyFrameAction.class);

    @Nonnull
    private final Provider<ObjectPropertyFrameTranslator> translatorProvider;

    @Nonnull
    private final FrameComponentSessionRendererFactory rendererFactory;

    @Nonnull
    private final PropertyValueComparator propertyValueComparator;

    @Inject
    public GetObjectPropertyFrameActionHandler(@Nonnull AccessManager accessManager,
                                               @Nonnull Provider<ObjectPropertyFrameTranslator> translatorProvider,
                                               @Nonnull FrameComponentSessionRendererFactory rendererFactory,
                                               @Nonnull PropertyValueComparator propertyValueComparator) {
        super(accessManager);
        this.rendererFactory = rendererFactory;
        this.translatorProvider = translatorProvider;
        this.propertyValueComparator = propertyValueComparator;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(GetObjectPropertyFrameAction action) {
        return VIEW_PROJECT;
    }

    @Nonnull
    public GetObjectPropertyFrameResult execute(@Nonnull GetObjectPropertyFrameAction action, @Nonnull ExecutionContext executionContext) {
        var translator = translatorProvider.get();
        var plainFrame = translator.getFrame(action.getSubject());
        var renderedFrame = plainFrame.toEntityFrame(rendererFactory.create(), propertyValueComparator);
        logger.info(BROWSING,
                     "{} {} retrieved ObjectProperty frame for {}",
                     action.getProjectId(),
                     executionContext.getUserId(),
                     action.getSubject());
        return GetObjectPropertyFrameResult.create(renderedFrame);
    }

    @Nonnull
    @Override
    public Class<GetObjectPropertyFrameAction> getActionClass() {
        return GetObjectPropertyFrameAction.class;
    }
}
