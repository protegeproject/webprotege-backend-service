package edu.stanford.protege.webprotege.sharing;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInCapability;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import jakarta.inject.Inject;

import static edu.stanford.protege.webprotege.access.BuiltInCapability.EDIT_SHARING_SETTINGS;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 07/02/15
 */
public class GetProjectSharingSettingsActionHandler extends AbstractProjectActionHandler<GetProjectSharingSettingsAction, GetProjectSharingSettingsResult> {

    private final ProjectSharingSettingsManager sharingSettingsManager;

    @Inject
    public GetProjectSharingSettingsActionHandler(ProjectSharingSettingsManager sharingSettingsManager, AccessManager accessManager) {
        super(accessManager);
        this.sharingSettingsManager = sharingSettingsManager;
    }

    @Nullable
    @Override
    protected BuiltInCapability getRequiredExecutableBuiltInAction(GetProjectSharingSettingsAction action) {
        return EDIT_SHARING_SETTINGS;
    }

    @Nonnull
    @Override
    public GetProjectSharingSettingsResult execute(@Nonnull GetProjectSharingSettingsAction action, @Nonnull ExecutionContext executionContext) {
        ProjectSharingSettings settings = sharingSettingsManager.getProjectSharingSettings(action.projectId());
        return new GetProjectSharingSettingsResult(settings);
    }

    @Nonnull
    @Override
    public Class<GetProjectSharingSettingsAction> getActionClass() {
        return GetProjectSharingSettingsAction.class;
    }
}
