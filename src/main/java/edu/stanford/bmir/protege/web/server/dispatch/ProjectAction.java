package edu.stanford.bmir.protege.web.server.dispatch;

import edu.stanford.bmir.protege.web.server.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.server.project.HasProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/02/2013
 */
@ProjectSingleton
public interface ProjectAction<R extends Result> extends Action<R>, HasProjectId {

}
