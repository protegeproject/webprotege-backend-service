package edu.stanford.protege.webprotege.crud;

import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.common.ProjectId;

import javax.annotation.Nonnull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/19/13
 */
public class GetEntityCrudKitSettingsAction implements ProjectAction<GetEntityCrudKitSettingsResult> {

    public static final String CHANNEL = "webprotege.entities.GetEntityCrudKitSetings";

    private ProjectId projectId;

    public GetEntityCrudKitSettingsAction(ProjectId projectId) {
        this.projectId = projectId;
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }
}
