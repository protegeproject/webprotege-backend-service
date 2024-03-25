package edu.stanford.protege.webprotege.frame;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.frame.translator.DataPropertyFrameTranslator;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Provider;

import static edu.stanford.protege.webprotege.access.BuiltInAction.VIEW_CHANGES;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public class GetDataPropertyFrameActionHandler extends AbstractProjectActionHandler<GetDataPropertyFrameAction, GetDataPropertyFrameResult> {

    @Nonnull
    private final Provider<DataPropertyFrameTranslator> translatorProvider;

    @Nonnull
    private final PlainFrameRenderer plainFrameRenderer;

    @Inject
    public GetDataPropertyFrameActionHandler(@Nonnull AccessManager accessManager,
                                             @Nonnull Provider<DataPropertyFrameTranslator> translatorProvider,
                                             @Nonnull PlainFrameRenderer plainFrameRenderer) {
        super(accessManager);
        this.translatorProvider = translatorProvider;
        this.plainFrameRenderer = plainFrameRenderer;
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
        var plainFrame = translator.getFrame(action.subject());
        var renderedFrame = plainFrameRenderer.toDataPropertyFrame(plainFrame);
        return new GetDataPropertyFrameResult(renderedFrame);
    }

    @Nonnull
    @Override
    public Class<GetDataPropertyFrameAction> getActionClass() {
        return GetDataPropertyFrameAction.class;
    }
}
