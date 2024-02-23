package edu.stanford.protege.webprotege.frame;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.dispatch.Action;
import edu.stanford.protege.webprotege.frame.translator.NamedIndividualFrameTranslator;
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
 * Date: 20/02/2013
 */
public class GetNamedIndividualFrameActionHandler extends AbstractProjectActionHandler<GetNamedIndividualFrameAction, GetNamedIndividualFrameResult> {

    @Nonnull
    private final Provider<NamedIndividualFrameTranslator> translatorProvider;

    @Nonnull
    private final PlainFrameRenderer plainFrameRenderer;

    @Inject
    public GetNamedIndividualFrameActionHandler(@Nonnull AccessManager accessManager,
                                                @Nonnull Provider<NamedIndividualFrameTranslator> translatorProvider,
                                                @Nonnull PlainFrameRenderer plainFrameRenderer) {
        super(accessManager);
        this.translatorProvider = translatorProvider;
        this.plainFrameRenderer = plainFrameRenderer;
    }

    /**
     * Gets the class of {@link Action} handled by this handler.
     * @return The class of {@link Action}.  Not {@code null}.
     */
    @Nonnull
    @Override
    public Class<GetNamedIndividualFrameAction> getActionClass() {
        return GetNamedIndividualFrameAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(GetNamedIndividualFrameAction action) {
        return VIEW_PROJECT;
    }

    @Nonnull
    @Override
    public GetNamedIndividualFrameResult execute(@Nonnull GetNamedIndividualFrameAction action,
                                                 @Nonnull ExecutionContext executionContext) {
        var translator = translatorProvider.get();
        translator.setMinimizePropertyValues(true);
        var plainFrame = translator.getFrame(action.subject());
        var renderedFrame = plainFrameRenderer.toNamedIndividualFrame(plainFrame);
        return new GetNamedIndividualFrameResult(renderedFrame);

    }
}
