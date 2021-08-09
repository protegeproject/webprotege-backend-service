package edu.stanford.protege.webprotege.sharing;

import edu.stanford.protege.webprotege.common.ProjectId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
public interface ProjectSharingSettingsManager {

    void setProjectSharingSettings(ProjectSharingSettings projectSharingSettings);

    ProjectSharingSettings getProjectSharingSettings(ProjectId projectId);
}
