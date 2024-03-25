package edu.stanford.protege.webprotege.frame;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.frame.translator.ObjectPropertyFrameTranslator;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Provider;

import static edu.stanford.protege.webprotege.access.BuiltInAction.VIEW_PROJECT;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public class GetObjectPropertyFrameActionHandler extends AbstractProjectActionHandler<GetObjectPropertyFrameAction, GetObjectPropertyFrameResult> {

    @Nonnull
    private final Provider<ObjectPropertyFrameTranslator> translatorProvider;

    @Nonnull
    private final PlainFrameRenderer plainFrameRenderer;

    @Inject
    public GetObjectPropertyFrameActionHandler(@Nonnull AccessManager accessManager,
                                               @Nonnull Provider<ObjectPropertyFrameTranslator> translatorProvider,
                                               @Nonnull PlainFrameRenderer plainFrameRenderer) {
        super(accessManager);
        this.plainFrameRenderer = plainFrameRenderer;
        this.translatorProvider = translatorProvider;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(GetObjectPropertyFrameAction action) {
        return VIEW_PROJECT;
    }

    @Nonnull
    public GetObjectPropertyFrameResult execute(@Nonnull GetObjectPropertyFrameAction action, @Nonnull ExecutionContext executionContext) {
        var translator = translatorProvider.get();
        var plainFrame = translator.getFrame(action.subject());
        var renderedFrame = plainFrameRenderer.toObjectPropertyFrame(plainFrame);
        return new GetObjectPropertyFrameResult(action.projectId(), renderedFrame);
    }

    @Nonnull
    @Override
    public Class<GetObjectPropertyFrameAction> getActionClass() {
        return GetObjectPropertyFrameAction.class;
    }
}
