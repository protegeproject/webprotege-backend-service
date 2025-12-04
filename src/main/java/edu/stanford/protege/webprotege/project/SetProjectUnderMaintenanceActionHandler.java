package edu.stanford.protege.webprotege.project;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInCapability;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.dispatch.ProjectActionHandlerComponent;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import jakarta.inject.Inject;

import static edu.stanford.protege.webprotege.access.BuiltInCapability.EDIT_PROJECT_SETTINGS;

/**
 * Handler for setting the under maintenance status of a project.
 */
public class SetProjectUnderMaintenanceActionHandler extends AbstractProjectActionHandler<SetProjectUnderMaintenanceAction, SetProjectUnderMaintenanceResult> {

    @Nonnull
    private final ProjectDetailsManager projectDetailsManager;

    @Inject
    public SetProjectUnderMaintenanceActionHandler(@Nonnull AccessManager accessManager,
                                                   @Nonnull ProjectDetailsManager projectDetailsManager) {
        super(accessManager);
        this.projectDetailsManager = projectDetailsManager;
    }

    @Nonnull
    @Override
    public Class<SetProjectUnderMaintenanceAction> getActionClass() {
        return SetProjectUnderMaintenanceAction.class;
    }

    @Nullable
    @Override
    protected BuiltInCapability getRequiredExecutableBuiltInAction(SetProjectUnderMaintenanceAction action) {
        return EDIT_PROJECT_SETTINGS;
    }

    @Nonnull
    @Override
    public SetProjectUnderMaintenanceResult execute(@Nonnull SetProjectUnderMaintenanceAction action, @Nonnull ExecutionContext executionContext) {
        projectDetailsManager.setUnderMaintenance(action.projectId(), action.isUnderMaintenance());
        ProjectDetails projectDetails = projectDetailsManager.getProjectDetails(action.projectId());
        return SetProjectUnderMaintenanceResult.create(projectDetails.isUnderMaintenance());
    }
}

