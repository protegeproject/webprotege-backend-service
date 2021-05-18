package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.server.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.server.dispatch.Result;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/02/2013
 */
public abstract class UpdateFrameAction implements ProjectAction<Result> {

    public abstract PlainEntityFrame getFrom();

    public abstract PlainEntityFrame getTo();
}
