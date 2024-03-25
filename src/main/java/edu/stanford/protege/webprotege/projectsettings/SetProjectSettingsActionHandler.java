package edu.stanford.protege.webprotege.projectsettings;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.dispatch.ProjectActionHandlerComponent;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.project.ProjectDetailsManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import static edu.stanford.protege.webprotege.access.BuiltInAction.EDIT_PROJECT_SETTINGS;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25/11/14
 */
@ProjectActionHandlerComponent
public class SetProjectSettingsActionHandler extends AbstractProjectActionHandler<SetProjectSettingsAction, SetProjectSettingsResult> {

    @Nonnull
    private final ProjectDetailsManager projectDetailsManager;

    @Inject
    public SetProjectSettingsActionHandler(@Nonnull AccessManager accessManager,
                                           @Nonnull ProjectDetailsManager projectDetailsManager) {
        super(accessManager);
        this.projectDetailsManager = projectDetailsManager;
    }

    @Nonnull
    @Override
    public Class<SetProjectSettingsAction> getActionClass() {
        return SetProjectSettingsAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(SetProjectSettingsAction action) {
        return EDIT_PROJECT_SETTINGS;
    }

    @Nonnull
    @Override
    public SetProjectSettingsResult execute(@Nonnull SetProjectSettingsAction action, @Nonnull ExecutionContext executionContext) {
        projectDetailsManager.setProjectSettings(action.getProjectSettings());
        return SetProjectSettingsResult.create(projectDetailsManager.getProjectSettings(action.projectId()));
    }
}
