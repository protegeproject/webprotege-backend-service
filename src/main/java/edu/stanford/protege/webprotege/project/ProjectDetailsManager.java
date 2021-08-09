package edu.stanford.protege.webprotege.project;

import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.projectsettings.ProjectSettings;
import edu.stanford.protege.webprotege.common.UserId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
public interface ProjectDetailsManager {
    /**
     * Creates a new project.
     * @param newProjectSettings The {@link NewProjectSettings} that describes the new project.  Not <code>null</code>.
     */
    void registerProject(ProjectId projectId, NewProjectSettings newProjectSettings);

    ProjectDetails getProjectDetails(ProjectId projectId) throws UnknownProjectException;

    boolean isExistingProject(ProjectId projectId);

    boolean isProjectOwner(UserId userId, ProjectId projectId);

    void setInTrash(ProjectId projectId, boolean b);

    ProjectSettings getProjectSettings(ProjectId projectId);

    void setProjectSettings(ProjectSettings projectSettings);


}
