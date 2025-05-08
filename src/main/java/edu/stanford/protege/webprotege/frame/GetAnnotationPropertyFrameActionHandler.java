package edu.stanford.protege.webprotege.frame;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInCapability;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.frame.translator.AnnotationPropertyFrameTranslator;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import jakarta.inject.Inject;
import jakarta.inject.Provider;

import static edu.stanford.protege.webprotege.access.BuiltInCapability.VIEW_PROJECT;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public class GetAnnotationPropertyFrameActionHandler extends AbstractProjectActionHandler<GetAnnotationPropertyFrameAction, GetAnnotationPropertyFrameResult> {

    private final Logger logger = LoggerFactory.getLogger(GetAnnotationPropertyFrameActionHandler.class);

    @Nonnull
    private final Provider<AnnotationPropertyFrameTranslator> translatorProvider;

    @Nonnull
    private final PlainFrameRenderer plainFrameRenderer;

    @Inject
    public GetAnnotationPropertyFrameActionHandler(@Nonnull AccessManager accessManager,
                                                   @Nonnull Provider<AnnotationPropertyFrameTranslator> translatorProvider,
                                                   @Nonnull PlainFrameRenderer plainFrameRenderer) {
        super(accessManager);
        this.plainFrameRenderer = plainFrameRenderer;
        this.translatorProvider = translatorProvider;
    }

    @Nullable
    @Override
    protected BuiltInCapability getRequiredExecutableBuiltInAction(GetAnnotationPropertyFrameAction action) {
        return VIEW_PROJECT;
    }

    @Nonnull
    @Override
    public GetAnnotationPropertyFrameResult execute(@Nonnull GetAnnotationPropertyFrameAction action, @Nonnull ExecutionContext executionContext) {
        var translator = translatorProvider.get();
        var plainFrame = translator.getFrame(action.subject());
        var renderedFrame = plainFrameRenderer.toAnnotationPropertyFrame(plainFrame);
        return new GetAnnotationPropertyFrameResult(renderedFrame);
    }

    @Nonnull
    @Override
    public Class<GetAnnotationPropertyFrameAction> getActionClass() {
        return GetAnnotationPropertyFrameAction.class;
    }
}
