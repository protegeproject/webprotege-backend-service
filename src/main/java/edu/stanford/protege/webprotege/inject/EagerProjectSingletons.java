package edu.stanford.protege.webprotege.inject;

import edu.stanford.protege.webprotege.dispatch.impl.ProjectActionHandlerRegistry;
import edu.stanford.protege.webprotege.index.impl.IndexUpdater;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.revision.RevisionManager;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-02
 *
 * This isn't used for anything other than to force the eager instantiation of certain objects
 * in the project component object graph.
 */
@ProjectSingleton
public class EagerProjectSingletons {

    private final ProjectId projectId;

    private final RevisionManager revisionManager;

    private final IndexUpdater indexUpdater;

    private final ProjectActionHandlerRegistry projectActionHandlerRegistry;

    @Inject
    public EagerProjectSingletons(ProjectId projectId,
                                  RevisionManager revisionManager,
                                  IndexUpdater indexUpdater,
                                  ProjectActionHandlerRegistry projectActionHandlerRegistry) {
        this.projectId = projectId;
        this.revisionManager = revisionManager;
        this.indexUpdater = indexUpdater;
        this.projectActionHandlerRegistry = projectActionHandlerRegistry;
    }
}
