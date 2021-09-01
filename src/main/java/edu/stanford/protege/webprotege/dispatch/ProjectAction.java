package edu.stanford.protege.webprotege.dispatch;

import edu.stanford.protege.webprotege.common.ProjectRequest;
import edu.stanford.protege.webprotege.inject.ProjectSingleton;
import edu.stanford.protege.webprotege.project.HasProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/02/2013
 */
@ProjectSingleton
public interface ProjectAction<R extends Result> extends Action<R>, HasProjectId, ProjectRequest<R> {

}
