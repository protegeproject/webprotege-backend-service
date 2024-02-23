package edu.stanford.protege.webprotege.frame;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.change.ChangeApplicationResult;
import edu.stanford.protege.webprotege.change.ChangeListGenerator;
import edu.stanford.protege.webprotege.change.HasApplyChanges;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectChangeHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.mansyntax.ManchesterSyntaxChangeGeneratorFactory;
import edu.stanford.protege.webprotege.renderer.RenderingManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Optional;

import static edu.stanford.protege.webprotege.access.BuiltInAction.EDIT_ONTOLOGY;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 18/03/2014
 */
public class SetManchesterSyntaxFrameActionHandler extends AbstractProjectChangeHandler<Optional<ManchesterSyntaxFrameParseError>, SetManchesterSyntaxFrameAction, SetManchesterSyntaxFrameResult> {

    @Nonnull
    private final GetManchesterSyntaxFrameActionHandler handler;

    @Nonnull
    private final RenderingManager renderer;

    @Nonnull
    private final ManchesterSyntaxChangeGeneratorFactory factory;

    @Inject
    public SetManchesterSyntaxFrameActionHandler(@Nonnull AccessManager accessManager,

                                                 @Nonnull HasApplyChanges applyChanges,
                                                 @Nonnull GetManchesterSyntaxFrameActionHandler handler,
                                                 @Nonnull RenderingManager renderer,
                                                 @Nonnull ManchesterSyntaxChangeGeneratorFactory factory) {
        super(accessManager, applyChanges);
        this.handler = handler;
        this.renderer = renderer;
        this.factory = factory;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(SetManchesterSyntaxFrameAction action) {
        return EDIT_ONTOLOGY;
    }

    @Override
    protected ChangeListGenerator<Optional<ManchesterSyntaxFrameParseError>> getChangeListGenerator(SetManchesterSyntaxFrameAction action,
                                                                                                    ExecutionContext executionContext) {
        return factory.create(
                renderer.getRendering(action.getSubject()),
                action.getFromRendering(),
                action.getToRendering(),
                action.getCommitMessage().orElse(""),
                action);
    }

    @Override
    protected SetManchesterSyntaxFrameResult createActionResult(ChangeApplicationResult<Optional<ManchesterSyntaxFrameParseError>> result,
                                                                SetManchesterSyntaxFrameAction action,
                                                                ExecutionContext executionContext) {

        if (result.getSubject().isPresent()) {
            throw new SetManchesterSyntaxFrameException(result.getSubject().get());
        }
        else {
            var ac = GetManchesterSyntaxFrameAction.create(action.projectId(),
                                                        action.getSubject());
            GetManchesterSyntaxFrameResult frame = handler.execute(ac, executionContext);
            String reformattedFrame = frame.getFrameManchesterSyntax();
            return SetManchesterSyntaxFrameResult.create(reformattedFrame);
        }
    }

    @Nonnull
    @Override
    public Class<SetManchesterSyntaxFrameAction> getActionClass() {
        return SetManchesterSyntaxFrameAction.class;
    }


}
