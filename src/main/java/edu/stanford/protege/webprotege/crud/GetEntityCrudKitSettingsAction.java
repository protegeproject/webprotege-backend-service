package edu.stanford.protege.webprotege.crud;

import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.project.ProjectId;

import javax.annotation.Nonnull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/19/13
 */
public class GetEntityCrudKitSettingsAction implements ProjectAction<GetEntityCrudKitSettingsResult> {

    private ProjectId projectId;

    private GetEntityCrudKitSettingsAction() {
    }

    public GetEntityCrudKitSettingsAction(ProjectId projectId) {
        this.projectId = projectId;
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }
}
