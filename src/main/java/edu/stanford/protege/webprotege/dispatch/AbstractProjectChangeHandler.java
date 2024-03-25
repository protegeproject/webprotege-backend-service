package edu.stanford.protege.webprotege.dispatch;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.change.ChangeApplicationResult;
import edu.stanford.protege.webprotege.change.ChangeListGenerator;
import edu.stanford.protege.webprotege.change.HasApplyChanges;
import edu.stanford.protege.webprotege.common.Request;
import edu.stanford.protege.webprotege.common.Response;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/02/2013
 */
public abstract class AbstractProjectChangeHandler<T, A extends Request<R>, R extends Response> extends AbstractProjectActionHandler<A, R> {

    @Nonnull
    private final HasApplyChanges applyChanges;

    public AbstractProjectChangeHandler(@Nonnull AccessManager accessManager, @Nonnull HasApplyChanges applyChanges) {
        super(accessManager);
        this.applyChanges = checkNotNull(applyChanges);
    }

    @Nonnull
    @Override
    public final R execute(@Nonnull A action, @Nonnull ExecutionContext executionContext) {
        ChangeListGenerator<T> changeListGenerator = getChangeListGenerator(action, executionContext);
        ChangeApplicationResult<T> result = applyChanges.applyChanges(executionContext.userId(),
                                                                      changeListGenerator);
        return createActionResult(result, action, executionContext);
    }

    protected abstract ChangeListGenerator<T> getChangeListGenerator(A action,
                                                                     ExecutionContext executionContext);

    protected abstract R createActionResult(ChangeApplicationResult<T> changeApplicationResult,
                                            A action,
                                            ExecutionContext executionContext);



}
