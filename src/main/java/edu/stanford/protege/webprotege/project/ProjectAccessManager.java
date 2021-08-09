package edu.stanford.protege.webprotege.project;

import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 3 Mar 2017
 */
public interface ProjectAccessManager {
    void logProjectAccess(ProjectId projectId, UserId userId, long timestamp);
}
