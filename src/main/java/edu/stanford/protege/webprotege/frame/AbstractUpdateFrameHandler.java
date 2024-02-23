package edu.stanford.protege.webprotege.frame;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.change.HasApplyChanges;
import edu.stanford.protege.webprotege.common.Request;
import edu.stanford.protege.webprotege.common.Response;
import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/02/2013
 */
public abstract class AbstractUpdateFrameHandler<A extends Request<R> & UpdateFrame, R extends Response> extends AbstractProjectActionHandler<A, R> {

    @Nonnull
    private final HasApplyChanges applyChanges;

    @Nonnull
    private final FrameChangeGeneratorFactory frameChangeGeneratorFactory;

    public AbstractUpdateFrameHandler(@Nonnull AccessManager accessManager, @Nonnull HasApplyChanges applyChanges,
                                      @Nonnull FrameChangeGeneratorFactory frameChangeGeneratorFactory) {
        super(accessManager);
        this.applyChanges = applyChanges;
        this.frameChangeGeneratorFactory = frameChangeGeneratorFactory;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(A action) {
        return BuiltInAction.EDIT_ONTOLOGY;
    }

    /**
     * Executes the specified action, against the specified project in the specified context.
     * @param action The action to be handled/executed
     * @param executionContext The {@link edu.stanford.protege.webprotege.ipc.ExecutionContext;} that should be
     * used to provide details such as the
     * {@link UserId} of the user who requested the action be executed.
     * @return The result of the execution to be returned to the client.
     */
    @Nonnull
    @Override
    public R execute(@Nonnull A action, @Nonnull ExecutionContext executionContext) {
        var from = action.from();
        var to = action.to();
        if(from.equals(to)) {
            return createResponse(action.to());
        }
        var userId = executionContext.userId();
        var frameUpdate = FrameUpdate.get(from, to);
        var changeGenerator = frameChangeGeneratorFactory.create(action.changeRequestId(), frameUpdate);
        applyChanges.applyChanges(userId, changeGenerator);
        return createResponse(action.to());
    }

    protected abstract R createResponse(PlainEntityFrame to);
}
