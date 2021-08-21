package edu.stanford.protege.webprotege.frame;

import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.dispatch.Result;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/02/2013
 */
public interface UpdateFrameAction {

    PlainEntityFrame getFrom();

    PlainEntityFrame getTo();
}
