package edu.stanford.protege.webprotege.dispatch;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Jun 2017
 */
public interface ApplicationActionHandler<A extends Action<R>, R extends Result> extends ActionHandler<A, R> {

}
