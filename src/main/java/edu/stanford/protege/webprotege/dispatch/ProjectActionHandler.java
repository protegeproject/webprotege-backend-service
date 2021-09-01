package edu.stanford.protege.webprotege.dispatch;

import edu.stanford.protege.webprotege.common.Request;
import edu.stanford.protege.webprotege.common.Response;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Jun 2017
 */
public interface ProjectActionHandler<A extends Request<R>, R extends Response> extends ActionHandler<A, R> {

}
