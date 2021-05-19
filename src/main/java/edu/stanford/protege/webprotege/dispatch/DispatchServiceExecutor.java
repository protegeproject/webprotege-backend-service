package edu.stanford.protege.webprotege.dispatch;

import edu.stanford.protege.webprotege.permissions.PermissionDeniedException;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/02/2013
 */
public interface DispatchServiceExecutor {

    <A extends Action<R>, R extends Result> DispatchServiceResultContainer execute(A action, RequestContext requestContext, ExecutionContext executionContext) throws ActionExecutionException, PermissionDeniedException;
}
