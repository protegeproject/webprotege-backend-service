package edu.stanford.protege.webprotege.frame;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.dispatch.ExecutionContext;
import edu.stanford.protege.webprotege.frame.translator.DataPropertyFrameTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Comparator;

import static edu.stanford.protege.webprotege.access.BuiltInAction.VIEW_CHANGES;
import static edu.stanford.protege.webprotege.logging.Markers.BROWSING;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public class GetDataPropertyFrameActionHandler extends AbstractProjectActionHandler<GetDataPropertyFrameAction, GetDataPropertyFrameResult> {

    private static final Logger logger = LoggerFactory.getLogger(GetDataPropertyFrameActionHandler.class);

    @Nonnull
    private final Provider<DataPropertyFrameTranslator> translatorProvider;

    @Nonnull
    private final FrameComponentSessionRendererFactory rendererFactory;

    @Nonnull
    private final Comparator<PropertyValue> propertyValueComparator;

    @Inject
    public GetDataPropertyFrameActionHandler(@Nonnull AccessManager accessManager,
                                             @Nonnull Provider<DataPropertyFrameTranslator> translatorProvider,
                                             @Nonnull FrameComponentSessionRendererFactory rendererFactory,
                                             @Nonnull Comparator<PropertyValue> propertyValueComparator) {
        super(accessManager);
        this.translatorProvider = translatorProvider;
        this.rendererFactory = rendererFactory;
        this.propertyValueComparator = propertyValueComparator;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(GetDataPropertyFrameAction action) {
        return VIEW_CHANGES;
    }

    @Nonnull
    @Override
    public GetDataPropertyFrameResult execute(@Nonnull GetDataPropertyFrameAction action, @Nonnull ExecutionContext executionContext) {
        var translator = translatorProvider.get();
        var plainFrame = translator.getFrame(action.getSubject());
        var renderedFrame = plainFrame.toEntityFrame(rendererFactory.create(), propertyValueComparator);
        logger.info(BROWSING,
                    "{} {} retrieved DataProperty frame for {}",
                    action.getProjectId(),
                    executionContext.getUserId(),
                    action.getSubject());
        return GetDataPropertyFrameResult.create(renderedFrame);
    }

    @Nonnull
    @Override
    public Class<GetDataPropertyFrameAction> getActionClass() {
        return GetDataPropertyFrameAction.class;
    }
}
