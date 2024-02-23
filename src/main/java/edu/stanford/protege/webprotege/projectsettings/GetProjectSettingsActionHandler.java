package edu.stanford.protege.webprotege.projectsettings;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
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
public class GetProjectSettingsActionHandler extends AbstractProjectActionHandler<GetProjectSettingsAction, GetProjectSettingsResult> {


    @Nonnull
    private final ProjectId projectId;

    private final ProjectDetailsManager projectDetailsManager;

    @Inject
    public GetProjectSettingsActionHandler(@Nonnull AccessManager accessManager,
                                           @Nonnull ProjectId projectId,
                                           ProjectDetailsManager projectDetailsManager) {
        super(accessManager);
        this.projectId = projectId;
        this.projectDetailsManager = projectDetailsManager;
    }

    @Nonnull
    @Override
    public Class<GetProjectSettingsAction> getActionClass() {
        return GetProjectSettingsAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(GetProjectSettingsAction action) {
        return EDIT_PROJECT_SETTINGS;
    }

    @Nonnull
    @Override
    public GetProjectSettingsResult execute(@Nonnull GetProjectSettingsAction action, @Nonnull ExecutionContext executionContext) {
        return GetProjectSettingsResult.create(projectDetailsManager.getProjectSettings(projectId));
    }

}
