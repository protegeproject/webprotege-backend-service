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
public class SetProjectSharingSettingsActionHandler extends AbstractProjectActionHandler<SetProjectSharingSettingsAction, SetProjectSharingSettingsResult> {

    @Nonnull
    private final ProjectSharingSettingsManager sharingSettingsManager;

    @Inject
    public SetProjectSharingSettingsActionHandler(@Nonnull AccessManager accessManager,
                                                  @Nonnull ProjectSharingSettingsManager sharingSettingsManager) {
        super(accessManager);
        this.sharingSettingsManager = sharingSettingsManager;
    }

    @Nullable
    @Override
    protected BuiltInCapability getRequiredExecutableBuiltInAction(SetProjectSharingSettingsAction action) {
        return EDIT_SHARING_SETTINGS;
    }

    @Nonnull
    @Override
    public SetProjectSharingSettingsResult execute(@Nonnull SetProjectSharingSettingsAction action, @Nonnull ExecutionContext executionContext) {
        sharingSettingsManager.setProjectSharingSettings(action.settings());
        return new SetProjectSharingSettingsResult();
    }

    @Nonnull
    @Override
    public Class<SetProjectSharingSettingsAction> getActionClass() {
        return SetProjectSharingSettingsAction.class;
    }
}
