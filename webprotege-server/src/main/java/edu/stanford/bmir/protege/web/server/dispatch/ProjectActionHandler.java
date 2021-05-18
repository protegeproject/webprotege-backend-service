package edu.stanford.bmir.protege.web.server.dispatch;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Jun 2017
 */
public interface ProjectActionHandler<A extends ProjectAction<R>, R extends Result> extends ActionHandler<A, R> {

}
