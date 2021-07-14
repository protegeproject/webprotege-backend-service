package edu.stanford.protege.webprotege.frame;

import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.dispatch.Result;

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
